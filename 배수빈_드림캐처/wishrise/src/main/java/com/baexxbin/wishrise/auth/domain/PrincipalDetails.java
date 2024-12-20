package com.baexxbin.wishrise.auth.domain;

import com.baexxbin.wishrise.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/*
* OAuth2 인증을 사용한 로그인한 사용자 정보를 가짐
* Spring Security에서 OAuth2 인증을 통해 로그인한 사용자 정보를 처리하는 '사용자 정보 객체'
*
* OAuth2를 통해 로그인 시 Spring Security는 PrincipalDetails 객체 생성해 사용자 정보 관리
* OAuth2User 인터페이스 구현 : OAuth2 인증을 통해 로그인한 사용자 정보를 처리
* UserDetails 인터페이스 구현 : Spring Security에서 사용자 인증 및 권한을 처리할 때 필요한 정보 제공
* GrantedAuthority : Spring Security에서 권한을 확인하는 전용 객체
* */

public record PrincipalDetails(
        Member member,
        Map<String, Object> attributes, // OAuth2로 부터 받은 사용자 정보
        String attributeKey             // 사용자 정보 조회시 사용할 키
) implements OAuth2User, UserDetails {

    @Override
    public String getName() {
        return attributes.get(attributeKey).toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().getKey())
        );
    }

    @Override
    public String getPassword() {
        return null;
    }   // OAuth2 로그인에서는 비밀번호가 필요 없으므로 null

    @Override
    public String getUsername() {
        return member.getMemberKey();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }   // 계정 만료 여부

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }   // 계정 잠금 여부

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }   // 자격 증명 만료 여부

    @Override
    public boolean isEnabled() {
        return true;
    }               // 계정 활성화 여부


}
