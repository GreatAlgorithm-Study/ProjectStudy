package com.baexxbin.wishrise.auth.service;

import com.baexxbin.wishrise.auth.domain.Token;
import com.baexxbin.wishrise.auth.dto.LoginResponse;
import com.baexxbin.wishrise.auth.exception.TokenException;
import com.baexxbin.wishrise.auth.jwt.TokenProvider;
import com.baexxbin.wishrise.global.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.baexxbin.wishrise.global.exception.ErrorCode.TOKEN_EXPIRED;

/*
* Refresh Token : Redis에 저장
* Access Token : 클라이언트에서만 저장
* */
@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;

    private static final String REFRESH_TOKEN_PREFIX = "refresh:"; // Redis Key Prefix
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일 (밀리초)



    // Access Token 및 Refresh Token 생성
    @Transactional
    public Token generateTokens(Authentication authentication) {
        // Access Token 생성
        String accessToken = tokenProvider.generateAccessToken(authentication);

        // Refresh Token 생성
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // Refresh Token Redis 저장
        String redisKey = REFRESH_TOKEN_PREFIX + authentication.getName();
        redisUtils.setData(redisKey, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);

        return new Token(authentication.getName(), refreshToken, accessToken);
    }

    // Redis에서 Refresh Token 삭제
    public void deleteRefreshToken(String memberKey) {
        String redisKey = REFRESH_TOKEN_PREFIX + memberKey;
        redisUtils.deleteData(redisKey);
    }


    // Redis에서 Refresh Token 검증
    public boolean validateRefreshToken(String memberKey, String refreshToken) {
        String redisKey = REFRESH_TOKEN_PREFIX + memberKey;
        String storedRefreshToken = redisUtils.getData(redisKey);

        // Redis에 저장된 토큰과 비교 및 유효성 검증
        return refreshToken.equals(storedRefreshToken) && tokenProvider.validateToken(refreshToken);
    }


    // Access Token 재발급
    public String reissueAccessToken(String refreshToken) {
        // Refresh Token에서 인증 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        // Redis에서 Refresh Token 검증
        String memberKey = authentication.getName();
        if (!validateRefreshToken(memberKey, refreshToken)) {
            throw new TokenException(TOKEN_EXPIRED);
        }

        // Access Token 재발급
        return tokenProvider.generateAccessToken(authentication);
    }


    // 로그인 응답 생성
    public LoginResponse getLoginResponse(Authentication authentication) {
        // Access Token 및 Refresh Token 생성
        Token tokens = generateTokens(authentication);

        return LoginResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .userId(authentication.getName())
                .username(authentication.getPrincipal().toString())
                .role(authentication.getAuthorities().stream()
                        .findFirst()
                        .map(Object::toString)
                        .orElse("USER"))
                .build();
    }
}
