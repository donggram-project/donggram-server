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
    @GetMapping()
    public ResponseEntity getSelectedClubs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<Long> collegeIds,
            @RequestParam(required = false) List<Long> divisionIds,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ){
        ResponseDto clubs = clubService.findClubsByFilters(keyword, collegeIds, divisionIds, pageable);
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

    @PostMapping("/new")
    public ResponseEntity postNewClub(@RequestBody NewClubDto newClubDto){

        ResponseDto responseDto = clubService.postNewClub(newClubDto);

        return ResponseEntity.ok(responseDto);
    }
}
