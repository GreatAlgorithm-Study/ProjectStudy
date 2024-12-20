package com.projectStudy.kkogitcogit.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    // ROLE_: Spring Security 접두사
    GUEST("ROLE_GUEST"), // 비회원
    USER("ROLE_USER"),   // 회원
    ADMIN("ROLE_ADMIN")  // 관리자
    ;

    private final String key;
}
