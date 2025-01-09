package com.yeong.happyolive.global.oauth2;

import com.yeong.happyolive.auth.domain.Role;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * DefaultOAuth2User를 상속하고, nickname, email과 role 필드를 추가로 가진다.
 */
@Getter
@ToString
public class OAuth2User extends DefaultOAuth2User {

    private String nickname;
    private String email;
    private Role role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's "name" from
     *                         {@link #getAttributes()}
     */
    public OAuth2User(Collection<? extends GrantedAuthority> authorities,
                      Map<String, Object> attributes, String nameAttributeKey,
                      String nickname, String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
}