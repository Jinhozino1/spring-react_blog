package com.jin.board_back.dto.response.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jin.board_back.common.ResponseCode;
import com.jin.board_back.common.ResponseMessage;
import com.jin.board_back.dto.response.ResponseDto;
import com.jin.board_back.dto.response.user.data.GetUserData;
import com.jin.board_back.entity.UserEntity;

import lombok.Getter;

@Getter
public class PatchProfileImageResponseDto extends ResponseDto{

    private PatchProfileImageResponseDto (GetUserData data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // public PatchProfileImageResponseDto(String code, String message, GetUserData data) {
    //     super(code, message, data);
    // }

    public static ResponseEntity<PatchProfileImageResponseDto> success (UserEntity userEntity) {
        GetUserData data = GetUserData.fromEntity(userEntity);
        PatchProfileImageResponseDto result = new PatchProfileImageResponseDto(data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
    public static ResponseEntity<ResponseDto> signInRequired() {
        ResponseDto result = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
