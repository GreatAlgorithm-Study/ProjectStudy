package com.example.talktalk_be.domain.member.controller;

import com.example.talktalk_be.domain.member.dto.response.KakaoAuthCodeResponseDto;
import com.example.talktalk_be.domain.member.dto.response.KakaoTokenResponseDto;
import com.example.talktalk_be.domain.member.openFeignClient.KakaoAuthService;
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
    public ResponseEntity<KakaoAuthCodeResponseDto> getAuthCode() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-tokens")
    public ResponseEntity<KakaoTokenResponseDto> getAuthorizationCode(@RequestParam String code) {
        return ResponseEntity.ok(kakaoAuthService.getKakaoToken(code));
    }
}
