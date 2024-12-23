package com.projectStudy.kkogitcogit.domain.user.dto.response;

import com.projectStudy.kkogitcogit.domain.user.entity.UserEntity;

public record UserLoginResponse(
        String email,
        String name
) {
    public UserLoginResponse(UserEntity userEntity) {
        this(
                userEntity.getEmail(),
                userEntity.getName()
        );
    }
}
