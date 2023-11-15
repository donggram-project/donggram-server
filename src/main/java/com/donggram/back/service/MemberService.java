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

    public ResponseDto join(SignUpDto signUpDto) throws Exception{
        if(memberRepository.findByStudentId(signUpDto.getStudentId()).isPresent()){
            throw new Exception("이미 가입된 학번 입니다.");
        }
        if(!signUpDto.getPassword().equals(signUpDto.getCheckPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

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
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);


        return ResponseDto.builder()
                .status(200)
                .responseMessage("회원가입 성공")
                .data("NULL")
                .build();
    }

    public ResponseDto login(SignInDto signInDto) {
        Member member = memberRepository.findByStudentId(signInDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 학번입니다."));


        TokenInfo tokenInfo = jwtTokenProvider.generateToken(signInDto.getStudentId(), member.getRoles());

        //RefreshToken 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByStudentId(signInDto.getStudentId());

        //있으면 refreshToken 업데이트
        //없으면 새로 만들고 저장
        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenInfo.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenInfo.getRefreshToken(), signInDto.getStudentId());
            refreshTokenRepository.save(newToken);
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("로그인 성공!")
                .data(tokenInfo)
                .build();
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
    public ResponseDto updateDetails(String token, ProfileUpdateDto profileUpdateDto) {
        Member member = memberRepository.findByStudentId(jwtTokenProvider.getUserPk(token)).get();


        MultipartFile file = profileUpdateDto.getProfileImage();

        if (file != null && !file.isEmpty()) {
            ImageProfile imageProfile = uploadImage(file, member);
            member.updateImageProfile(imageProfile);
        }

        member.updateProfile(profileUpdateDto);


        return ResponseDto.builder()
                .status(200)
                .responseMessage("수정된 프로필 정보 API")
                .data(new MemberDetailsDto(member))
                .build();
    }

    private ImageProfile uploadImage(MultipartFile file, Member member) {
        try {
            String imageFileName = member.getId() + "_" + file.getOriginalFilename();

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


