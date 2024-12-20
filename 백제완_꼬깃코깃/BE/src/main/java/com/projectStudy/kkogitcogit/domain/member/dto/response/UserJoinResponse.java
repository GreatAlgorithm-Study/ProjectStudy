package com.projectStudy.kkogitcogit.domain.member.dto.response;

import com.projectStudy.kkogitcogit.domain.member.entity.UserEntity;
import java.util.UUID;

public record UserJoinResponse(
        UUID uuid,
        String email,
        String name
) {
    public UserJoinResponse(UserEntity userEntity) {
        this(
                userEntity.getUuid(),
                userEntity.getEmail(),
                userEntity.getName()
        );
    }
}
