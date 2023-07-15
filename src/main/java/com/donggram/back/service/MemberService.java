package com.donggram.back.service;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.dto.SignUpDto;
import com.donggram.back.entity.Member;
import com.donggram.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseDto join(SignUpDto signUpDto) throws Exception{
        if(memberRepository.findById(signUpDto.getStudent_id()).isPresent()){
            throw new Exception("이미 가입된 학번 입니다.");
        }
        if(!signUpDto.getPassword().equals(signUpDto.getCheckPassword())){
            throw new Exception("비밀번호가 일치 하지 않습니다.");
        }

        Member member = Member.builder()
                .id(signUpDto.getStudent_id())
                .password(signUpDto.getPassword())
                .name(signUpDto.getName())
                .college1(signUpDto.getCollege1())
                .college2(signUpDto.getCollege2())
                .major1(signUpDto.getMajor1())
                .major2(signUpDto.getMajor2())
                .profile_image("NULL")
                .build();
        memberRepository.save(member);
        member.encodePassword(passwordEncoder);

        return ResponseDto.builder()
                .status(200)
                .responseMessage("회원가입 성공")
                .data("NULL")
                .build();
    }
}
