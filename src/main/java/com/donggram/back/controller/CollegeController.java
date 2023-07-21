package com.donggram.back.controller;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.service.CollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colleges")
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService collegeService;

    @GetMapping("/all")
    public ResponseEntity getAllCollege(){
        ResponseDto allCollege = collegeService.getAllCollege();

        return ResponseEntity.ok(allCollege);
    }

    @GetMapping
    public ResponseEntity getCollege(@RequestParam List<Long> collegeIds){

        return ResponseEntity.ok("good");
    }

}
