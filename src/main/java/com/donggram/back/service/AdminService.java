package com.donggram.back.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.donggram.back.dto.*;
import com.donggram.back.entity.*;
import com.donggram.back.repository.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final MemberRepository memberRepository;
    private final ClubRequestRepository clubRequestRepository;
    private final CollegeRepository collegeRepository;
    private final DivisionRepository divisionRepository;
    private final ClubRepository clubRepository;
    private final AmazonS3Client amazonS3Client;
    private final ImageClubRepository imageClubRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ResponseDto getAllMembers() {

        List<MemberListDto> memberList = new ArrayList<>();
        for (Member member : memberRepository.findAll()) {
            memberList.add(MemberListDto.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .major(member.getMajor1())
                    .role(member.getRoles().get(0))
                    .studentId(member.getStudentId())
                    .build());
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("멤버 목록 API")
                .data(memberList)
                .build();
    }

    @Transactional
    public ResponseDto getMemberDetails(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // 가입한 동아리
            HashMap<String, String> clubList = new HashMap<>();
            for (ClubJoin clubJoin : member.getClubJoinList()) {
                String clubName = clubJoin.getClub().getClubName();
                RequestStatus status = clubJoin.getStatus();
                clubList.put(clubName, status.name());
            }
            MemberDetailsDto memberDetailsDto = MemberDetailsDto.builder()
                    .memberId(member.getId())
                    .studentId(member.getStudentId())
                    .memberName(member.getName())
                    .college1(member.getCollege1())
                    .major1(member.getMajor1())
                    .college2(member.getCollege2())
                    .major2(member.getMajor2())
                    .role(member.getRoles().get(0))
                    .clubList(clubList)
                    .build();
            if(member.getImageProfile() != null){
                memberDetailsDto.updateProfileImage(member.getImageProfile().getUrl());
            }

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
    public ResponseDto modifySelectedMember(Long memberId, ProfileUpdateDto requestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 멤버가 존재하지 않습니다."));
        member.updateProfile(requestDto);

        return ResponseDto.builder()
                .status(200)
                .responseMessage("상세 페이지 업데이트 완료")
                .data("NULL")
                .build();
    }

    @Transactional
    public ResponseDto deleteSelectedMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        memberRepository.delete(member);
        return ResponseDto.builder()
                .status(200)
                .responseMessage("삭제된 멤버 ID")
                .data(memberId)
                .build();
    }

    //동아리 생성 요청 API 처리 로직

    //모두 조회
    @Transactional
    @JsonIgnore
    public ResponseDto getAllClubs() {
        List<ClubListDto> clubList = new ArrayList<>();
        for (ClubRequest clubRequest : clubRequestRepository.findAll()) {
            clubList.add(ClubListDto.builder()
                    .id(clubRequest.getId())
                    .club_name(clubRequest.getClubName())
                    .student_name(clubRequest.getMember().getName())
                    .apply_date(clubRequest.getClub_created())
                    .major(clubRequest.getMember().getMajor1())
                    .status(clubRequest.getStatus().toString())
                    .build());
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("멤버 목록 API")
                .data(clubList)
                .build();
    }

    @Transactional
    public ResponseDto getClubDetails(Long clubId){
        Optional<ClubRequest> clubRequestOptional = clubRequestRepository.findById(clubId);
        System.out.println(clubId);

        if (clubRequestOptional.isPresent()) {
            // 동아리 가져옴
            ClubRequest clubRequest = clubRequestOptional.get();

            // 동아리 세부정보 양식
            ClubDetailsDto clubDetailsDto = ClubDetailsDto.builder()
                    .clubCreated(clubRequest.getClub_created())
                    .clubId(clubRequest.getId())
                    .clubName(clubRequest.getClubName())
                    .content(clubRequest.getContent())
                    .college(clubRequest.getCollege())
                    .division(clubRequest.getDivision())
                    .ClubImage(clubRequest.getImageClub().getUrl())
                    .isRecruitment(clubRequest.isRecruitment())
                    .writer(clubRequest.getMember().getName())
                    .recruitmentPeriod(clubRequest.getRecruitment_period())
                    .build();


            return ResponseDto.builder()
                    .status(200)
                    .responseMessage("동아리 세부정보 API")
                    .data(clubDetailsDto)
                    .build();
        } else {
            return ResponseDto.builder()
                    .status(400)
                    .responseMessage("해당 동아리 정보 NULL")
                    .data("NULL")
                    .build();
        }
    }

    @Transactional
    public ResponseDto approve(Long clubRequestId) {

        ClubRequest clubRequest = clubRequestRepository.findById(clubRequestId)
                .orElseThrow(() -> new RuntimeException("해당 동아리 생성 요청이 존재하지 않습니다."));

        // 동아리 상태 변경
        clubRequest.updateStatus(RequestStatus.approve);

        College college = collegeRepository.findByName(clubRequest.getCollege()).orElseThrow(() -> new RuntimeException("해당 단과대가 존재하지 않습니다."));
        Division division = divisionRepository.findByName(clubRequest.getDivision()).orElseThrow(() -> new RuntimeException("해당 분과가 존재하지 않습니다."));

        // 작성자 동아리 자동 가입
        Member member = clubRequest.getMember();

        Club club = Club.builder()
                .clubName(clubRequest.getClubName())
                .college(college)
                .division(division)
                .recruitment_period(clubRequest.getRecruitment_period())
                .content(clubRequest.getContent())
                .clubJoinList(new ArrayList<>())
                .isRecruitment(clubRequest.isRecruitment())
                .clubCreated(clubRequest.getClub_created())
                .clubRequest(clubRequest)
                .build();

        club.setImageClub(clubRequest.getImageClub());

        clubRepository.save(club);

        ClubJoin clubJoin = ClubJoin.builder()
                .club(club)
                .joinDate(ClubService.LocalDateTimeToString())
                .role(Role.HOST)
                .member(member)
                .build();

        club.updateClubJoinList(clubJoin);

        return ResponseDto.builder()
                .responseMessage("동아리 생성 승인 완료")
                .status(200)
                .data("NULL").build();

    }

    @Transactional
    public ResponseDto reject(Long clubRequestId){
        ClubRequest clubRequest = clubRequestRepository.findById(clubRequestId)
                .orElseThrow(() -> new RuntimeException("해당 동아리 생성 요청이 존재하지 않습니다."));

        clubRequest.updateStatus(RequestStatus.rejected);

        return ResponseDto.builder()
                .data("NULL")
                .status(200)
                .responseMessage("동아리 생성 요청 거절").build();
    }

    @Transactional
    public ResponseDto getAllMemberBySelectedClub(Long clubRequestId){
        List<ClubMemberDto> clubMemberDtos = new ArrayList<>();

        //받아오는게 ClubRequestId

        ClubRequest clubRequest = clubRequestRepository.findById(clubRequestId).orElseThrow(() -> new RuntimeException("해당 동아리요청 엔티티가 존재하지 않습니다."));

        //보내줘야 되는게 clubId

        long clubId = clubRequest.getClub().getId();

        Club club = clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("해당 동아리가 존재하지 않습니다."));
        for ( ClubJoin clubJoin : club.getClubJoinList()) {
            ClubMemberDto clubMemberDto = ClubMemberDto.builder()
                    .division(clubJoin.getMember().getMajor1())
                    .name(clubJoin.getMember().getName())
                    .join_dated(clubJoin.getJoinDate())
                    .id(clubJoin.getMember().getId())
                    .status(clubJoin.getStatus())
                    .build();

            clubMemberDtos.add(clubMemberDto);
        }

        return ResponseDto.builder()
                .data(clubMemberDtos)
                .status(200)
                .responseMessage("동아리 멤버 목록 API")
                .build();
    }

    @Transactional
    public ResponseDto updateClubDetails(Long clubRequestId, ClubProfileUpdateDto clubProfileUpdateDto, MultipartFile multipartFile){
        ClubRequest clubRequest = clubRequestRepository.findById(clubRequestId).orElseThrow(() -> new RuntimeException("해당 동아리생성 엔티티가 존재하지 않습니다."));

        Club club = clubRequest.getClub();

        club.updateClubProfile(clubProfileUpdateDto, collegeRepository, divisionRepository);

        System.out.println("1111111111111111");

        ImageClub imageClub = imageClubRepository.findByClubId(club.getId()).orElseThrow(() -> new RuntimeException("해당 동아리이미지 엔티티가 존재하지 않습니다."));


        if (!multipartFile.isEmpty() && multipartFile != null){
            System.out.println("akdljf,x.cnvklzcjiajgi;e");
            String updateImageUrl = uploadImage(multipartFile, clubRequest);
            imageClub.uploadCustomImage(updateImageUrl);
        } else {
            System.out.println("imageProfile Null");
            imageClub.uploadCustomImage(club.getImageClub().getUrl());
        }

        ClubDetailsDto clubDetailsDto = ClubDetailsDto.builder()
                .clubId(club.getId())
                .clubName(club.getClubName())
                .clubCreated(club.getClubCreated())
                .college(club.getCollege().getName())
                .division(club.getDivision().getName())
                .ClubImage(club.getImageClub().getUrl())
                .content(club.getContent())
                .writer(club.getClubRequest().getMember().getName())
                .recruitmentPeriod(club.getRecruitment_period())
                .isRecruitment(club.isRecruitment())
                .build();

        return ResponseDto.builder()
                .data(clubDetailsDto)
                .status(200)
                .responseMessage("동아리 멤버 목록 API")
                .build();
    }
    private String uploadImage(MultipartFile file, ClubRequest clubRequest) {
        try {
            String imageFileName = "club_" + clubRequest.getId() + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, imageFileName, file.getInputStream(), metadata);

            String imageUrl = "https://image-profile-bucket.s3.ap-northeast-2.amazonaws.com/" + imageFileName;

            return imageUrl;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseDto deleteSelectedClub(Long clubRequestId) {
        ClubRequest clubRequest = clubRequestRepository.findById(clubRequestId).orElseThrow(() -> new RuntimeException("해당 동아리요청 엔티티가 존재하지 않습니다."));

        clubRepository.delete(clubRequest.getClub());
        return ResponseDto.builder()
                .status(200)
                .responseMessage("삭제된 동아리 ID")
                .data(clubRequestId)
                .build();
    }


}



