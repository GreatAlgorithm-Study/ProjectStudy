package com.example.talktalk_be.domain.member.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String name;
}
