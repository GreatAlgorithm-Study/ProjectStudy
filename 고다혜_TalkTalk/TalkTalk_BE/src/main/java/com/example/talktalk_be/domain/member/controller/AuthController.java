package com.example.talktalk_be.domain.member.controller;

import com.example.talktalk_be.domain.member.dto.response.KakaoMemberInfoResponseDto;
import com.example.talktalk_be.domain.member.dto.response.KakaoRedirectUrlResponseDto;
import com.example.talktalk_be.domain.member.dto.response.KakaoTokenResponseDto;
import com.example.talktalk_be.domain.member.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/kakao")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/redirect")
    public ResponseEntity<KakaoRedirectUrlResponseDto> getAuthCode() {
        return ResponseEntity.ok(kakaoAuthService.getRedirectUrl());
    }

    @GetMapping("/access_token")
    public ResponseEntity<KakaoTokenResponseDto> getAuthorizationCode(@RequestParam String code) {
        return ResponseEntity.ok(kakaoAuthService.getToken(code));
    }

    // 토큰 정보 보기로 액세스 토큰의 유효성을 검증한 이후, 사용자 정보 가져오기를 요청해
    // 필요한 사용자 정보를 받아 서비스 회원 가입 및 로그인 진행
    @GetMapping("/login")
    public ResponseEntity<KakaoMemberInfoResponseDto> loginByToken(@RequestParam("access_token") String accessToken) {
        return ResponseEntity.ok(kakaoAuthService.loginByToken(accessToken));
    }
}
