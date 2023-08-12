package com.donggram.back.service;

import com.donggram.back.dto.ClubDto;
import com.donggram.back.dto.MemberListDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.Member;
import com.donggram.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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
}
