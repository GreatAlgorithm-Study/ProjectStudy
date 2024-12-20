package com.example.talktalk_be.domain.member.openFeignClient;

import com.example.talktalk_be.domain.member.dto.response.KakaoTokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-auth-service", url = "https://kauth.kakao.com/oauth")
public interface KakaoAuthClient {

    @Operation(summary = "카카오 로그인을 위한 토큰을 받는 api")
    @PostMapping("/token")
    KakaoTokenResponseDto getKakaoToken(@RequestParam("grant_type") String grantType,
                                        @RequestParam("code") String code,
                                        @RequestParam("client_id") String clientId,
                                        @RequestParam("redirect_uri") String redirectUri,
                                        @RequestParam("client_secret") String clientSecret);
}
