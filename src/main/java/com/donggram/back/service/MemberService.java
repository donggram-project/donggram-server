package com.donggram.back.service;

import com.donggram.back.dto.*;
import com.donggram.back.entity.Club;
import com.donggram.back.entity.ClubJoin;
import com.donggram.back.entity.Member;
import com.donggram.back.entity.RefreshToken;
import com.donggram.back.jwt.JwtTokenProvider;
import com.donggram.back.repository.MemberRepository;
import com.donggram.back.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseDto join(SignUpDto signUpDto) throws Exception{
        if(memberRepository.findByStudentId(signUpDto.getStudentId()).isPresent()){
            throw new Exception("이미 가입된 학번 입니다.");
        }
        if(!signUpDto.getPassword().equals(signUpDto.getCheckPassword())){
            throw new Exception("비밀번호가 일치 하지 않습니다.");
        }

        Member member = Member.builder()
                .studentId(signUpDto.getStudentId())
                .password(signUpDto.getPassword())
                .name(signUpDto.getName())
                .college1(signUpDto.getCollege1())
                .college2(signUpDto.getCollege2())
                .major1(signUpDto.getMajor1())
                .major2(signUpDto.getMajor2())
                .profileImage("NULL")
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
                .responseMessage("로그인 성공")
                .data(tokenInfo)
                .build();
    }
    
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




}


