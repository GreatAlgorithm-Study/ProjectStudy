package com.yeong.happyolive.auth.domain;

import com.yeong.happyolive.global.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

@Getter
@Entity
@NoArgsConstructor
@Table(name="user")
public class User extends BaseEntity {
    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @UuidGenerator(style = RANDOM)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    private UUID uuid;

    // name: 사용자 설정 이름
    @Setter
    @Column(nullable = true)
    private String name;

    // nickname : 소셜로그인으로 부터 가져오는 이름 정보
    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    public User(String nickname, String email, SocialType socialType, String socialId) {
//        this.uuid = UUID.randomUUID();
        this.nickname = nickname;
        this.email = email;
        this.role= Role.USER;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", socialId='" + socialId + '\'' +
                ", socialType=" + socialType +
                '}';
    }
}
