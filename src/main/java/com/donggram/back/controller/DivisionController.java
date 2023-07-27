package com.donggram.back.controller;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.service.CollegeService;
import com.donggram.back.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/division")
@RequiredArgsConstructor
public class DivisionController {

    private final DivisionService divisionService;

    @GetMapping("/all")
    public ResponseEntity getAllDivision(){
        ResponseDto allDivision = divisionService.getAllDivision();

        return ResponseEntity.ok(allDivision);
    }

}
