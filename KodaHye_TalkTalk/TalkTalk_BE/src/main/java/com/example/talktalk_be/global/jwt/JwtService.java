package com.example.talktalk_be.global.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface JwtService {
    String createAccessToken(String username);
    String createRefreshToken();
    void updateRefreshToken(String username, String refreshToken);
    void destroyRefreshToken(String username);

//    void sendToken(HttpServletResponse response, String accessToken, String refreshToken) throws Exception;
    void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);
    void sendAccessToken(HttpServletResponse response, String accessToken);

    Optional<String> extractAccessToken(HttpServletRequest request) throws IOException, ServletException;
    Optional<String> extractRefreshToken(HttpServletRequest request) throws IOException, ServletException;
    Optional<String> extractUsername(String accessToken);
    void setAccessTokenHeader(HttpServletResponse response, String accessToken);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshTokens);
    boolean isTokenValid(String token);
}
