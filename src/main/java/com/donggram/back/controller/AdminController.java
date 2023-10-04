package com.donggram.back.controller;

import com.donggram.back.dto.ProfileUpdateDto;
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

    @GetMapping("/members/{id}")
    public ResponseEntity getSelectedMember(@PathVariable("id") Long memberId){
        ResponseDto selectedMember = adminService.getMemberDetails(memberId);
        return ResponseEntity.ok(selectedMember);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity modifySelectedMember(@PathVariable("id") Long memberId, @RequestBody ProfileUpdateDto profileUpdateDto){
        ResponseDto selectedMember = adminService.modifySelectedMember(memberId, profileUpdateDto);
        return ResponseEntity.ok(selectedMember);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity deleteMember(@PathVariable("id") Long memberId) {
        ResponseDto deletedMember = adminService.deleteSelectedMember(memberId);
        return ResponseEntity.ok(deletedMember);
    }

    @GetMapping("/clubs/all")
    public ResponseEntity getAllClubs(){
        ResponseDto allClubs = adminService.getAllClubs();
        return ResponseEntity.ok(allClubs);
    }

    @GetMapping("/clubs/{id}")
    public ResponseEntity getSelectedClub(@PathVariable("id") Long clubId){
        ResponseDto clubDetails = adminService.getClubDetails(clubId);
        return ResponseEntity.ok(clubDetails);
    }

    @PutMapping("/clubs/{id}/approve")
    public ResponseEntity approveClubCreation(@PathVariable Long id){
        ResponseDto approve = adminService.approve(id);
        return ResponseEntity.ok(approve);
    }

    @PutMapping("/clubs/{id}/reject")
    public ResponseEntity rejectClubCreation(@PathVariable Long id){
        ResponseDto reject = adminService.reject(id);
        return ResponseEntity.ok(reject);
    }
}
