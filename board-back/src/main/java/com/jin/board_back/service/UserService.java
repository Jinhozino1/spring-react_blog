package com.jin.board_back.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jin.board_back.dto.request.user.*;
import com.jin.board_back.dto.response.user.*;


@Service
public interface UserService {

    ResponseEntity<? super GetUserResponseDto> getUser(String email);
    ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email);
    ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto, String email);
    ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto dto, String email);
}