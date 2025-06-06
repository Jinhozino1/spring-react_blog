package com.jin.board_back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.jin.board_back.common.ResponseCode;
import com.jin.board_back.config.CustomUserDetails;
import com.jin.board_back.dto.request.user.PatchNicknameRequestDto;
import com.jin.board_back.dto.request.user.PatchProfileImageRequestDto;

import com.jin.board_back.dto.response.user.*;
import com.jin.board_back.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<? super GetUserResponseDto> getUser (
        @PathVariable("email") String email
    ) {
        if (email == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(ResponseCode.AUTHORIZATION_FAIL);
        ResponseEntity<? super GetUserResponseDto> response = userService.getUser(email);
        return response;
    }

    @GetMapping("")
    public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser (
        @AuthenticationPrincipal CustomUserDetails userDetails
     ) {
        if (userDetails == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(ResponseCode.AUTHORIZATION_FAIL);
        }

        String email = userDetails.getEmail();
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(ResponseCode.AUTHORIZATION_FAIL);
        }

        ResponseEntity<? super GetSignInUserResponseDto> response = userService.getSignInUser(email);
        return response;
    }

    @PatchMapping("/nickname")
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname (
        @RequestBody @Valid PatchNicknameRequestDto requestBody,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) return PatchNicknameResponseDto.signInRequired();
        String email = userDetails.getEmail();
        ResponseEntity<? super PatchNicknameResponseDto> response = userService.patchNickname(requestBody, email);
        return response;
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage (
        @RequestBody @Valid PatchProfileImageRequestDto requestDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) return PatchProfileImageResponseDto.signInRequired();
        String email = userDetails.getEmail();
        ResponseEntity<? super PatchProfileImageResponseDto> response = userService.patchProfileImage(requestDto,email);
        return response;
    }

}
