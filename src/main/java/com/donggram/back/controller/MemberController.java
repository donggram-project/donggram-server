package com.donggram.back.controller;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.dto.SignInDto;
import com.donggram.back.dto.SignUpDto;
import com.donggram.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody SignUpDto signUpDto) throws Exception {

        ResponseDto responseDto = memberService.join(signUpDto);

        return ResponseEntity.ok(responseDto);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto) throws Exception {

        ResponseDto responseDto = memberService.login(signInDto);

        return ResponseEntity.ok(responseDto);
    }
}
