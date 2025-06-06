package com.jin.board_back.dto.response.user.data;

import com.jin.board_back.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetSignInUserData {
    private String email;
    private String nickname;
    private String profileImage;

    public static GetSignInUserData fromEntity(UserEntity userEntity) {
        return new GetSignInUserData(
            userEntity.getEmail(),
            userEntity.getNickname(),
            userEntity.getProfileImage()
        );
    }
}
