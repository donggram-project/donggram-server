package com.donggram.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @PostMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("hello! test!");
        }
}

