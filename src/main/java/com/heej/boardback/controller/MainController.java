package com.heej.boardback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 서버 체크용 (test)
@RestController
@RequestMapping("")
public class MainController {

    @GetMapping("")
    public ResponseEntity<String> serverCheck() {
        return ResponseEntity.status(HttpStatus.OK).body("Server On...");
    }
    
}
