package com.jin.board_back.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jin.board_back.common.ResponseCode;
import com.jin.board_back.common.ResponseMessage;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class CustomResponseDto<T> {
    
    private String code;
    private String message;
    private T data;

    public static <T> ResponseEntity<CustomResponseDto<T>> databaseError() {
        CustomResponseDto<T> responseBody = new CustomResponseDto<>(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static <T> ResponseEntity<CustomResponseDto<T>> validationFailed() {
        CustomResponseDto<T> responseBody = new CustomResponseDto<>(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}