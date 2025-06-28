package com.jin.board_back.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jin.board_back.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;

import com.jin.board_back.dto.request.auth.SignInRequestDto;
import com.jin.board_back.dto.request.auth.SignUpRequestDto;
import com.jin.board_back.dto.response.auth.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(
        @RequestBody @Valid SignUpRequestDto requestbody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestbody);
        return response;
    }
    
    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn(
        @RequestBody @Valid SignInRequestDto requestBody,
        HttpServletResponse cookieResponse
        ) {
            ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody, cookieResponse);
            return response;
    }
    
}
