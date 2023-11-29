package com.donggram.back.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.donggram.back.dto.*;
import com.donggram.back.entity.*;
import com.donggram.back.jwt.JwtTokenProvider;
import com.donggram.back.repository.ImageProfileRepository;
import com.donggram.back.repository.MemberRepository;
import com.donggram.back.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AmazonS3Client amazonS3Client;
    private final ImageProfileRepository imageProfileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public ResponseDto join(SignUpDto signUpDto) throws Exception {
        if (memberRepository.findByStudentId(signUpDto.getStudentId()).isPresent()) {
            throw new Exception("이미 가입된 학번입니다.");
        }
        if (!signUpDto.getPassword().equals(signUpDto.getCheckPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        ImageProfile imageProfile = new ImageProfile();
        imageProfile.uploadBasicImage();

        Member member = Member.builder()
                .studentId(signUpDto.getStudentId())
                .password(signUpDto.getPassword())
                .name(signUpDto.getName())
                .college1(signUpDto.getCollege1())
                .college2(signUpDto.getCollege2())
                .major1(signUpDto.getMajor1())
                .major2(signUpDto.getMajor2())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        member.setImageProfile(imageProfile); // Member 객체에 ImageProfile 설정

        // 영속성 컨텍스트에 Member 객체 저장
        memberRepository.save(member);

        imageProfile.setMember(member); // ImageProfile 객체에 Member 설정

        // 영속성 컨텍스트에 ImageProfile 객체 저장
        imageProfileRepository.save(imageProfile);

        member.encodePassword(passwordEncoder);

        // 이미지 프로필 URL 출력 (테스트용)
        System.out.println(member.getImageProfile().getUrl());

        return ResponseDto.builder()
                .status(200)
                .responseMessage("회원가입 성공")
                .data("NULL")
                .build();
    }


    public ResponseDto login(SignInDto signInDto) {
        Member member = memberRepository.findByStudentId(signInDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 학번입니다."));

        // 입력된 비밀번호 확인 (예: BCrypt 해시 함수 사용)
        if (passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            // 비밀번호 일치 시 로그인 처리

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(signInDto.getStudentId(), member.getRoles());

            // RefreshToken 있는지 확인
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByStudentId(signInDto.getStudentId());

            // 있으면 refreshToken 업데이트
            // 없으면 새로 만들고 저장
            if (refreshToken.isPresent()) {
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenInfo.getRefreshToken()));
            } else {
                RefreshToken newToken = new RefreshToken(tokenInfo.getRefreshToken(), signInDto.getStudentId());
                refreshTokenRepository.save(newToken);
            }

            return ResponseDto.builder()
                    .status(200)
                    .responseMessage("로그인 성공!")
                    .data(tokenInfo)
                    .build();
        } else {
            // 비밀번호가 일치하지 않을 경우 처리
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    
    @Transactional
    public ResponseDto getMemberDetails(String token){


        // 해당 멤버 가져오기 By Token
        Optional<Member> memberOptional = memberRepository.findByStudentId(jwtTokenProvider.getUserPk(token));

        if(memberOptional.isPresent()){
            Member member = memberOptional.get();
            // 가입한 동아리
            HashMap<String, String> clubList = new HashMap<>();
            for (ClubJoin clubJoin: member.getClubJoinList()) {
                String clubName = clubJoin.getClub().getClubName();
                RequestStatus status = clubJoin.getStatus();
                clubList.put(clubName, status.name());
            }
            String profileImageUrl = member.getImageProfile() != null ? member.getImageProfile().getUrl() : null;
            System.out.println(member.getImageProfile().getUrl());

            MemberDetailsDto memberDetailsDto = MemberDetailsDto.builder()
                    .memberId(member.getId())
                    .studentId(member.getStudentId())
                    .memberName(member.getName())
                    .college1(member.getCollege1())
                    .major1(member.getMajor1())
                    .college2(member.getCollege2())
                    .major2(member.getMajor2())
                    .profileImage(profileImageUrl)
                    .clubList(clubList)
                    .build();

            return ResponseDto.builder()
                    .status(200)
                    .responseMessage("멤버 세부정보 API")
                    .data(memberDetailsDto)
                    .build();
        } else {
            return ResponseDto.builder()
                    .status(400)
                    .responseMessage("해당 멤버 정보 NULL")
                    .data("NULL")
                    .build();
        }
    }

    @Transactional
    public ResponseDto getClubList(String token){
        Member member = memberRepository.findByStudentId(jwtTokenProvider.getUserPk(token)).get();

        List<ClubDto> clubListDtos = new ArrayList<>();

        for (ClubJoin clubJoin : member.getClubJoinList()){
            Club club = clubJoin.getClub();

            if (clubJoin.getStatus().name() == "approve"){
                clubListDtos.add(ClubDto.builder()
                        .clubId(club.getId())
                        .clubName(club.getClubName())
                        .college(club.getCollege().getName())
                        .division(club.getDivision().getName())
                        .clubImage(club.getImageClub().getUrl())
                        .isRecruitment(club.isRecruitment())
                        .build());
            }
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("내 동아리 정보 API")
                .data(clubListDtos)
                .build();
    }

    @Transactional
    public ResponseDto updateDetails(String token, MultipartFile multipartFile) {
        Member member = memberRepository.findByStudentId(jwtTokenProvider.getUserPk(token)).get();
        if (multipartFile != null) {
            MultipartFile file = multipartFile;
            if (file != null && !file.isEmpty()) {
                ImageProfile imageProfile = uploadImage(file, member);
                member.updateImageProfile(imageProfile);
            }
        } else if (multipartFile.isEmpty()){
            ImageProfile imageProfile = new ImageProfile();
            imageProfile.uploadBasicImage();
            member.updateImageProfile(imageProfile);
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("수정된 프로필 정보 API")
                .data(new MemberDetailsDto(member))
                .build();
    }

    private ImageProfile uploadImage(MultipartFile file, Member member) {
        try {


            String code = UUID.randomUUID().toString();

            String imageFileName = "member_" + member.getId() + "_" + code;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, imageFileName, file.getInputStream(), metadata);

            ImageProfile image = ImageProfile.builder()
                    .url("https://image-profile-bucket.s3.ap-northeast-2.amazonaws.com/" + imageFileName)
                    .member(member)
                    .build();

            ImageProfile save = imageProfileRepository.save(image);
            return save;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


