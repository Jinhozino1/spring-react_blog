package com.jin.board_back.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jin.board_back.common.ResponseCode;
import com.jin.board_back.common.ResponseMessage;
import com.jin.board_back.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class SignInResponseDto extends ResponseDto{
    private String token;
    private int expirationTime;

    private SignInResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        // this.token = token;
        // this.expirationTime = 3600;
    }

    public static ResponseEntity<SignInResponseDto> success () {
        SignInResponseDto result = new SignInResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> signInFail() {
        ResponseDto result = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
