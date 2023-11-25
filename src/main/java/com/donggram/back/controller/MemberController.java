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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid SignUpDto signUpDto) throws Exception {

        ResponseDto responseDto = memberService.join(signUpDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto) throws Exception {

        ResponseDto responseDto = memberService.login(signInDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/member")
    public ResponseEntity<?> getSelectedMember(@RequestHeader("Access_Token") String token){

        ResponseDto memberDetails = memberService.getMemberDetails(token);
        return ResponseEntity.ok(memberDetails);
    }

    @PutMapping("/member")
    public ResponseEntity<?> updateProfile(@RequestHeader("Access_Token") String token, @RequestPart(value = "profileImage") MultipartFile imageFile) {

//        profileUpdateDto.addMultipartFile(imageFile);
        ResponseDto memberDetails = memberService.updateDetails(token, imageFile);
        return ResponseEntity.ok(memberDetails);
    }


}
