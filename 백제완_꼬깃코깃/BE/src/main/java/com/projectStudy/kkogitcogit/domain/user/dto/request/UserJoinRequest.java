package com.projectStudy.kkogitcogit.domain.user.dto.request;

import com.projectStudy.kkogitcogit.domain.user.entity.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UserJoinRequest(
        String email,
        String name,
        String password
) {
    public UserEntity toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return UserEntity.builder()
                .email(this.email)
                .name(this.name)
                .password(bCryptPasswordEncoder.encode(this.password))
                .build();
    }
}
