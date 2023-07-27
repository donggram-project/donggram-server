package com.donggram.back.controller;

import com.donggram.back.dto.ResponseDto;
import com.donggram.back.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/majors")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @GetMapping("/all")
    public ResponseEntity getAllMajor() {
        ResponseDto allMajor = majorService.getAllMajor();

        return ResponseEntity.ok(allMajor);
    }
}
