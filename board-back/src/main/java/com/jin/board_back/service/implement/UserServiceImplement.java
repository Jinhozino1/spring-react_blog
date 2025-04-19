package com.jin.board_back.service.implement;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.jin.board_back.dto.response.ResponseDto;
import com.jin.board_back.dto.response.user.GetSignInUserResponseDto;
import com.jin.board_back.entity.UserEntity;
import com.jin.board_back.repository.UserRepository;
import com.jin.board_back.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService{
    
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email) {
        
        // UserEntity userEntity = null;

        try {
            Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) return GetSignInUserResponseDto.notExistUser();
            UserEntity userEntity = optionalUser.get();
            return GetSignInUserResponseDto.sucess(userEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        // return GetSignInUserResponseDto.sucess(optionalUser);
    }
}
