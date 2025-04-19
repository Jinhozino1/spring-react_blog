package com.jin.board_back.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jin.board_back.dto.response.user.GetSignInUserResponseDto;

@Service
public interface UserService {

    ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email);
    
}