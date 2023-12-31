package com.heej.boardback.service;

import org.springframework.http.ResponseEntity;

import com.heej.boardback.dto.request.auth.SignInRequestDto;
import com.heej.boardback.dto.request.auth.SignUpRequestDto;
import com.heej.boardback.dto.response.auth.SignInResponseDto;
import com.heej.boardback.dto.response.auth.SignUpResponseDto;

public interface AuthService {
    
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto); 
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);

}
