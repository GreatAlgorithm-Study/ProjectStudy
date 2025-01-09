package com.yeong.happyolive.global.handler;

import com.yeong.happyolive.auth.domain.User;
import com.yeong.happyolive.auth.repository.UserRepository;
import com.yeong.happyolive.global.jwt.service.JwtService;
import com.yeong.happyolive.global.oauth2.OAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Value("${jwt.login-success-uri}")
    private String URI;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
            response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//            response.setHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
            String refreshToken = jwtService.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급
            response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
//            response.setHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);


            // v2 Redis에 저장 : Redis Cache에 저장
            Long refreshExpiration = jwtService.extractExpiration(refreshToken).get().getTime();
            jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken, refreshExpiration);
            log.info("로그인에 성공하였습니다. 이메일 : {}", oAuth2User.getEmail());
            log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
            log.info("로그인에 성공하였습니다. RefreshToken : {}", refreshToken);
            log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);


            // 토큰 전달을 위한 redirect
            String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                    .queryParam("atk", accessToken)
                    .queryParam("rtk", refreshToken)
                    .build().toUriString();
            response.sendRedirect(redirectUrl);


        } catch (Exception e) {
            throw e;
        }

    }

//    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
//    private void loginSuccess(HttpServletResponse response, OAuth2User oAuth2User) throws IOException {
//        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
//        String refreshToken = jwtService.createRefreshToken();
//        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
//
//        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
//    }
}
