package com.donggram.back.controller;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.dto.TokenInfo;
import com.donggram.back.entity.RefreshToken;
import com.donggram.back.jwt.JwtTokenProvider;
import com.donggram.back.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/refreshToken")
    public ResponseEntity generateToken(@RequestHeader("Refresh_Token") String requestHeader){
        Optional<RefreshToken> refreshToken1 = refreshTokenRepository.findByRefreshToken(requestHeader);
        String loginId = refreshToken1.get().getStudentId();
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(loginId, Collections.singletonList("ROLE_USER"));
        String newAccessToken = tokenInfo.getAccessToken();

        Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(ResponseDto.builder()
                        .status(201)
                        .responseMessage("New AccessToken Generated")
                .data(newAccessToken)
                .build());
    }
}
