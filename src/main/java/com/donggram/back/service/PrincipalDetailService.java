package com.donggram.back.service;

import com.donggram.back.entity.Member;
import com.donggram.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailService.loadUserByUsername");
        log.info("LOGIN");
        Member member = memberRepository.findByStudentId(username)
                .orElseThrow(() -> new RuntimeException("해당 멤버가 존재하지 않습니다."));

        return member;
    }
}
