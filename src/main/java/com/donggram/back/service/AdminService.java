package com.donggram.back.service;

import com.donggram.back.dto.ClubDto;
import com.donggram.back.dto.MemberDetailsDto;
import com.donggram.back.dto.MemberListDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.ClubJoin;
import com.donggram.back.entity.Member;
import com.donggram.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final MemberRepository memberRepository;

    public ResponseDto getAllMembers(){

        List<MemberListDto> memberList = new ArrayList<>();
        for (Member member : memberRepository.findAll()) {
            memberList.add(MemberListDto.builder()
                            .id(member.getId())
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
    public ResponseDto getMemberDetails(Long memberId){
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if(memberOptional.isPresent()){
            Member member = memberOptional.get();

            // 가입한 동아리
            List<String> clubList = new ArrayList<>();
            for (ClubJoin clubJoin: member.getClubJoinList()) {
                String clubName = clubJoin.getClub().getClubName();
                clubList.add(clubName);
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
                    .profileImage(member.getProfileImage())
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

    public ResponseDto deleteMember(Long memberId){
        memberRepository.deleteById(memberId);

        return ResponseDto.builder()
                .status(200)
                .responseMessage("멤버 삭제 API")
                .data("삭제 완료")
                .build();
    }
}