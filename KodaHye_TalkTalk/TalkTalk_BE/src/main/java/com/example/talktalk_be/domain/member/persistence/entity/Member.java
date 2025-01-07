package com.example.talktalk_be.domain.member.persistence.entity;

import com.example.talktalk_be.domain.member.persistence.entity.enums.Role;
import com.example.talktalk_be.domain.member.persistence.entity.enums.SocialType;
import com.example.talktalk_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200, unique = true)
    private String email;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 200)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String profileImagePath; // 이미지 처음에는 없음

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // Kakao, Naver, Google
    private Long socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인: null)
    private String refreshToken;

    @Builder
    public Member(Long id, String email, String password, String nickname, String name, String phoneNumber,
                  String profileImagePath, Role role, SocialType socialType, Long socialId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImagePath = profileImagePath;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }
}
