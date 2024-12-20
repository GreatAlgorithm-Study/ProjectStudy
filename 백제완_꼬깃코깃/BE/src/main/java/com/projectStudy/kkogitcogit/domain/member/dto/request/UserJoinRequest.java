package com.projectStudy.kkogitcogit.domain.member.dto.request;

import com.projectStudy.kkogitcogit.domain.member.entity.UserEntity;

public record UserJoinRequest(
        String email,
        String name,
        String password
) {
    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .name(this.name)
                .password(this.password)
                .build();
    }
}
