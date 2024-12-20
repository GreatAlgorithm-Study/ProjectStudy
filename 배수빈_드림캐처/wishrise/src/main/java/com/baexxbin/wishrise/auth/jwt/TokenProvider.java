package com.baexxbin.wishrise.auth.jwt;

import com.baexxbin.wishrise.auth.domain.Token;
import com.baexxbin.wishrise.auth.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.baexxbin.wishrise.global.exception.ErrorCode.INVALID_JWT_SIGNATURE;
import static com.baexxbin.wishrise.global.exception.ErrorCode.INVALID_TOKEN;

/*
* JWT 생성 및 검증만 담당하는 도메인 중립적인 '토큰 처리기' 역할
* */

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
//    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private static final String KEY_ROLE = "role";

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    // 내 서비스의 JWT accessToken 발급
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication);
    }

    // 1. refresh token 발급
//    public void generateRefreshToken(Authentication authentication, String accessToken) {
//        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
//        tokenService.saveOrUpdate(authentication.getName(), refreshToken, accessToken); // redis에 저장
//    }

    public String generateRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (1000 * 60 * 60 * 24 * 7)); // 7일간 유효

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }


    private String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TokenProvider.ACCESS_TOKEN_EXPIRE_TIME);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // 1. JWT에서 정보 추출
        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        // 2. 권한 생성
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(role)
        );

        // 3. JWT의 정보 Authentication에 전달
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {       // 토큰 만료
            return e.getClaims();               // 토큰 만료시에도 Claims 반환
        } catch (MalformedJwtException e) {     // 형식이 잘못된 토큰
            throw new TokenException(INVALID_TOKEN);
        } catch (SecurityException e) {         // 서명 검증 실패
            throw new TokenException(INVALID_JWT_SIGNATURE);
        }
    }

}
