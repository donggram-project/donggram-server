package com.donggram.back.controller;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    // 전체 동아리
    @GetMapping("/all")
    public ResponseEntity getAllClubs(){
        ResponseDto allClubs = clubService.getAllClubs();
        return ResponseEntity.ok(allClubs);

    }

    // 카테고리에서 선택했을 때
    @GetMapping()
    public ResponseEntity getSelectedClubs(@RequestParam List<Long> collegeIds){
        ResponseDto selectedClubs = clubService.getSelectedClubs(collegeIds);
        return ResponseEntity.ok(selectedClubs);
    }

    // 동아리 상세페이지
    @GetMapping("/{id}")
    public ResponseEntity getClubDetails(@PathVariable("id") Long clubId){
        ResponseDto clubDetails = clubService.getClubDetails(clubId);
        return ResponseEntity.ok(clubDetails);
    }
}
