package com.donggram.back.controller;

import com.donggram.back.dto.ClubDto;
import com.donggram.back.dto.NewClubDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.Club;
import com.donggram.back.jwt.JwtTokenProvider;
import com.donggram.back.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final JwtTokenProvider jwtTokenProvider;

    // 전체 동아리
    @GetMapping("/all")
    public ResponseEntity getAllClubs(){
        ResponseDto allClubs = clubService.getAllClubs();
        return ResponseEntity.ok(allClubs);
    }

    // 카테고리에서 선택했을 때
    @GetMapping("/search")
    public ResponseEntity getSelectedClubs(
          @RequestParam("keyword") String keyword
    ){
        ResponseDto clubs = clubService.findByKeyword(keyword);
        return ResponseEntity.ok(clubs);
    }

    // 동아리 상세페이지
    @GetMapping("/{id}")
    public ResponseEntity getClubDetails(@PathVariable("id") Long clubId){
        ResponseDto clubDetails = clubService.getClubDetails(clubId);
        return ResponseEntity.ok(clubDetails);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity postClubJoin(@PathVariable("id") Long clubId, @RequestHeader("Access_Token") String token){
        String jwt = token;
        String studentId = jwtTokenProvider.getUserPk(jwt);
        ResponseDto responseDto = clubService.postClubJoin(clubId, studentId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping()
    public ResponseEntity getSelectedClubs(@RequestParam("collegeIds") List<Long> collegeIds, @RequestParam("divisionIds") List<Long> divisionIds){
        ResponseDto selectedClubs = clubService.getSelectedClubs(collegeIds, divisionIds);
        return ResponseEntity.ok(selectedClubs);
    }

    @PostMapping("/new")
    public ResponseEntity postNewClub(@RequestBody NewClubDto newClubDto, @RequestHeader("Access_Token") String token){

        ResponseDto responseDto = clubService.postNewClub(newClubDto, token);

        return ResponseEntity.ok(responseDto);
    }
}
