package com.donggram.back.controller;

import com.donggram.back.dto.ProfileUpdateDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.dto.SignInDto;
import com.donggram.back.dto.SignUpDto;
import com.donggram.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
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

    @GetMapping("/members/{id}")
    public ResponseEntity<?> getSelectedMember(@PathVariable("id") Long memberId){
        ResponseDto memberDetails = memberService.getMemberDetails(memberId);
        return ResponseEntity.ok(memberDetails);
    }

    @PatchMapping("/members/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable("id") Long memberId, @ModelAttribute ProfileUpdateDto profileUpdateDto) {
        ResponseDto memberDetails = memberService.updateDetails(memberId, profileUpdateDto);
        return ResponseEntity.ok(memberDetails);
    }


}
