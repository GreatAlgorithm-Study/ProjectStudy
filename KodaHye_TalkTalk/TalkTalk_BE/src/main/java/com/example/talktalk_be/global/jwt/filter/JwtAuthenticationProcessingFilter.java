package com.example.talktalk_be.global.jwt.filter;

import com.example.talktalk_be.domain.member.persistence.entity.Member;
import com.example.talktalk_be.domain.member.persistence.respository.MemberRepository;
import com.example.talktalk_be.global.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
OncePerRequestFilter
- 모든 서블릿 컨테이너에서 요청 디스패치당 단일 실행을 보장하는 것을 목표로 하는 필터 기본 클래스
 */

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final String NO_CHECK_URL = "/login"; // /login으로 들어오는 요청에 대해서는 작동하지 않음

    /*
    1. 리프레시 토큰이 오는 경우
        - 유효하다면 AccessToken 재발급 후, 필터 진행 X
    2. 리프레시 토큰은 없고, AccessToken만 있는 경우
        - 유저 정보 저장 후 필터 계속 진행
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null); // RefreshToken이 없거나 유효하지 않다면 null 반환

        if(refreshToken != null) {
            // RefreshToken이 유효하다면 해당 refreshToken을 가진
            // 유저정보를 찾아오고, 존재한다면 AccessToken을 재발급
            // refreshToken만 보낸 경우에는 인증을 처리하지 안헥 하기 위해 바로 return 함
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // refreshToken이 없다면 AccessToken을 검사하는 로직
        // request에서 AccessToken을 추출한 후, 존재한다면 AccessToken을 통해 username 추출
        // username이 추출되었다면 해당 회원을 찾아서 그 정보를 가지고 인증 처리
        checkAccessTokenAndAuthentication(request, response, filterChain); // 4
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(
                accessToken -> jwtService.extractUsername(accessToken).ifPresent(
                        username -> memberRepository.findByEmail(username).ifPresent(
                                member -> saveAuthentication(member)
                        )
                )
        );

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        UserDetails user = User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
//                우선 role은 빼놓자!
//                .roles(member.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authoritiesMapper.mapAuthorities(user.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken).ifPresent(
                member -> jwtService.sendAccessToken(response, jwtService.createAccessToken(member.getEmail()))
        );

    }
}
