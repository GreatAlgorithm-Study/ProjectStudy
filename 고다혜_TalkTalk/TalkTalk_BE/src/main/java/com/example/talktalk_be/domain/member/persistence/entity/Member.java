package com.example.talktalk_be.domain.member.persistence.entity;

import com.example.talktalk_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loginId;

//    @Column(nullable = false, length = 200, unique = true)
    private String email;

//    @Column(nullable = false, length = 200)
    private String password;

//    @Column(nullable = false, length = 200)
    private String nickname;

//    @Column(nullable = false, length = 20)
    private String name;

//    @Column(nullable = false, length = 20)
    private String phoneNumber;

//    @Column(length = 20)
    private String profileImagePath; // 이미지 처음에는 없음

//    @Column(nullable = false, length = 2000)
//    @Column(length = 2000)
    private String token;

    // 소셜로그인 때문에 필요함
    @Column(length = 20)
    private String provider;
    @Column(length = 100)
    private String providerId;

    @Builder
    public Member(Long id, Long loginId, String email, String password, String nickname, String name, String phoneNumber, String profileImagePath, String token, String provider, String providerId) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImagePath = profileImagePath;
        this.token = token;
        this.provider = provider;
        this.providerId = providerId;
    }
}
