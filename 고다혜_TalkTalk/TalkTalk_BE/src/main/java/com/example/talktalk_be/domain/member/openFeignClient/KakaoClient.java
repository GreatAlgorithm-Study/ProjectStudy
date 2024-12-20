package com.example.talktalk_be.domain.member.openFeignClient;

import com.example.talktalk_be.domain.member.dto.response.KakaoMemberInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-service", url = "https://kapi.kakao.com")
public interface KakaoClient {
    @Operation(summary = "액세스 토큰으로 사용자 정보 가져오기")
    @GetMapping("/v2/user/me")
    KakaoMemberInfoResponseDto getInformation(@RequestHeader("Authorization") String accessToken);
}
