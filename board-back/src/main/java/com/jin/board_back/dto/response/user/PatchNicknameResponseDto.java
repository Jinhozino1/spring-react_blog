package com.jin.board_back.dto.response.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jin.board_back.common.ResponseCode;
import com.jin.board_back.common.ResponseMessage;
import com.jin.board_back.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class PatchNicknameResponseDto extends ResponseDto{
    
    private PatchNicknameResponseDto () {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
    // public PatchNicknameResponseDto(String code, String message, GetUserData data) {
    //     super(code, message, data);
    // }

    public static ResponseEntity<PatchNicknameResponseDto> success () {
        // GetUserData data = GetUserData.fromEntity(userEntity);
        PatchNicknameResponseDto result = new PatchNicknameResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    public static ResponseEntity<ResponseDto> duplicateNickname() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_NICKNAME, ResponseMessage.DUPLICATE_NICKNAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> signInRequired() {
        ResponseDto result = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
    

}
