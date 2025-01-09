package com.yeong.happyolive.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yeong.happyolive.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String RENEWAL_TIME_CLAIM = "renewalTime";
    private static final String NICKNAME = "nickname";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String nickname, String email) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + accessTokenExpirationPeriod);
        // ATK 토큰 만료 10분전(600000ms)
        Date renewalAt = new Date(expiresAt.getTime() - 600000);
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(expiresAt)  // 토큰 만료 시간 설정
                .withIssuedAt(now)         // 토큰 발급 시간 설정

                //클레임으로는 email, renewalTime(토큰 갱신 시간)
                //클레임 추가 => .withClaim(클래임 이름, 클래임 값) 으로 설정
                .withClaim(NICKNAME, nickname)
                .withClaim(EMAIL_CLAIM, email)
                .withClaim(RENEWAL_TIME_CLAIM, renewalAt)
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }
    /**
     * Redis에서 email로 RefreshToken 추출
     * */
    public String getRedisRefreshToken(String email) {
        return (String)redisTemplate.opsForValue().get(email);
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : < Bearer XXX >에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    /**
     * <email 얻기>
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(EMAIL_CLAIM) // claim(Emial) 가져오기
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * <nickname 얻기>
     * AccessToken에서 Nickname 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractNickname(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(NICKNAME) // claim(Emial) 가져오기
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * <ATK 만료 시간 얻기>
     * AccessToken에서 expiration 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 만료 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<Date> extractExpiration(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim("exp") // claim(exp) 가져오기
                    .asDate());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }


    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken, Long refreshExpiration) {
        redisTemplate.opsForValue().set(email, refreshToken, refreshExpiration, TimeUnit.MILLISECONDS);
    }

    /**
     * ATK 재발급 시, 이전 ATK 블랙리스트 등록
     * 남은 시간은 10분 이하
     * 왜냐하면 이전 ATK 만료 10분전에 사용자에게 재발급 여부 물어봄
     * */
    public void expireAccessToken(String accessToken) {
        Long expiration = extractExpiration(accessToken).get().getTime();
        Long curTime = new Date().getTime();
        Long remainTime = expiration - curTime;
        redisTemplate.opsForValue().set(accessToken, "logout", remainTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 블랙리스트 엑세스 토큰 유효성 검사
     * */
    private boolean validBlackToken(String accessToken) throws Exception {

        //Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.

        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackToken)){
            log.info("로그아웃 처리된 엑세스 토큰입니다.(블랙리스트 등록 토큰)");
            return false;
//            throw new Exception("로그아웃 처리된 엑세스 토큰입니다.(블랙리스트 등록 토큰)");
        }
        return true;
    }


    /**
     * 토큰 유효성 검사*/
    public boolean isTokenValid(String token, HttpServletRequest request) {
        log.info("[토큰 유효성 검사]");
        try {
            // 1. 토큰 유효성 검사
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            // 2. 블랙리스트 토큰 검사
            if (!validBlackToken(token)) {
                request.setAttribute("exception", HttpStatus.BAD_REQUEST);
                return false;
            };
            log.info("유효한 토큰 : "+token+"]");
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            request.setAttribute("exception", HttpStatus.BAD_REQUEST);
            return false;
        }
    }
}
