package com.donggram.back.controller;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.service.AdminService;
import com.donggram.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/members/all")
    public ResponseEntity getAllMembers(){
        ResponseDto allMembers = adminService.getAllMembers();
        return ResponseEntity.ok(allMembers);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity deleteMember(@PathVariable("id") Long memberId) {
        ResponseDto deletedMember = adminService.deleteSelectedMember(memberId);
        return ResponseEntity.ok(deletedMember);
    }

//    @GetMapping("/members/{id}")
//    public ResponseEntity getSelectedMember(@PathVariable("id") Long memberId){
//        ResponseDto selectedMember = adminService.getSelectedMember(memberId);
//    }


}
