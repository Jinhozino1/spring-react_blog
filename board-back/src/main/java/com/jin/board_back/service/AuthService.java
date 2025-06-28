package com.jin.board_back.service;

import org.springframework.http.ResponseEntity;

import com.jin.board_back.dto.request.auth.SignInRequestDto;
import com.jin.board_back.dto.request.auth.SignUpRequestDto;
import com.jin.board_back.dto.response.auth.SignUpResponseDto;

import jakarta.servlet.http.HttpServletResponse;

import com.jin.board_back.dto.response.auth.SignInResponseDto;

public interface AuthService {

    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response);
} 

