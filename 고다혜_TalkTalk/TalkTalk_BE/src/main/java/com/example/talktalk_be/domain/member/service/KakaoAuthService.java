package com.example.talktalk_be.domain.member.service;

import com.example.talktalk_be.domain.member.dto.response.KakaoMemberInfoResponseDto;
import com.example.talktalk_be.domain.member.dto.response.KakaoRedirectUrlResponseDto;
import com.example.talktalk_be.domain.member.dto.response.KakaoTokenResponseDto;
import com.example.talktalk_be.domain.member.openFeignClient.KakaoAuthClient;
import com.example.talktalk_be.domain.member.openFeignClient.KakaoClient;
import com.example.talktalk_be.domain.member.persistence.entity.Member;
import com.example.talktalk_be.domain.member.persistence.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class KakaoAuthService {

    @Value("${oauth.kakao.rest_api}")
    private String clientId;
    @Value("${oauth.kakao.redirect_uri}")
    private String redirectUri;
    @Value("${oauth.kakao.client_secret}")
    private String clientSecret;
    private String grantType = "authorization_code";

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoClient kakaoClient;
    private final MemberRepository memberRepository;

    public KakaoRedirectUrlResponseDto getRedirectUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://kauth.kakao.com/oauth/authorize?")
                .append("client_id=" + clientId)
                .append("&redirect_rul=" + redirectUri)
                .append("&response_type=code");

        return KakaoRedirectUrlResponseDto.builder()
                .url(sb.toString()).build();
    }

    public KakaoTokenResponseDto getToken(String code) {
        return kakaoAuthClient.getKakaoToken(grantType, code, clientId, redirectUri, clientSecret);
    }

    public KakaoMemberInfoResponseDto loginByToken(String accessToken) {

        KakaoMemberInfoResponseDto requestInfo = kakaoClient.getInformation("BEARER " + accessToken);

        // kakaoMemberId로 회원이 있다면 로그인. 없다면 회원가입.
        Optional<Member> member = memberRepository.findByLoginId(requestInfo.getId());
        if(member.isEmpty()) {
            Member saveMember = Member.builder()
                    .loginId(requestInfo.getId())
                    .nickname(requestInfo.getKakaoAccount().getProfile().getNickname()).build();
            memberRepository.save(saveMember);
        }
//        return kakaoClient.getInformation("BEARER " + accessToken);
        return requestInfo;
    }

}
