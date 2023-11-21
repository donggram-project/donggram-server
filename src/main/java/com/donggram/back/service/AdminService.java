package com.donggram.back.service;

import com.donggram.back.dto.*;
import com.donggram.back.entity.*;
import com.donggram.back.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseDto approve(Long clubId) {

        ClubRequest clubRequest = clubRequestRepository.findById(clubId)
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
                .build();

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
    public ResponseDto reject(Long clubId){
        ClubRequest clubRequest = clubRequestRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("해당 동아리 생성 요청이 존재하지 않습니다."));

        clubRequest.updateStatus(RequestStatus.rejected);

        return ResponseDto.builder()
                .data("NULL")
                .status(200)
                .responseMessage("동아리 생성 요청 거절").build();
    }

}



