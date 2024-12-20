package com.baexxbin.wishrise.auth.handler;

import com.baexxbin.wishrise.auth.domain.PrincipalDetails;
import com.baexxbin.wishrise.auth.domain.Token;
import com.baexxbin.wishrise.auth.dto.OAuth2UserInfo;
import com.baexxbin.wishrise.auth.service.TokenService;
import com.baexxbin.wishrise.member.application.MemberModuleService;
import com.baexxbin.wishrise.member.domain.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/*
* OAuth2 인증 과정에서 성공적인 인증 이후의 동작 정의
* */

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final MemberModuleService memberModuleService;
    private static final String URI = "/auth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. Authentication에서 PrincipalDetails 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 2. OAuth2User 정보 가져오기
        OAuth2User oAuth2User = principalDetails;

        // 3. OAuth2User에서 필요한 정보 추출
        String registrationId = oAuth2User.getName();                   // 소셜 로그인 제공자의 registrationId
        Map<String, Object> attributes = oAuth2User.getAttributes();    // 소셜 로그인 제공자에서 받은 사용자 정보

        // 4. OAuth2UserInfo 객체 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, attributes);

        // 5. loginOrJoin 호출하여 회원 정보 처리
        Member member = memberModuleService.loginOrJoin(oAuth2UserInfo);

        // 6. 새로운 PrincipalDetails 객체 생성
        PrincipalDetails newPrincipalDetails = new PrincipalDetails(member, attributes, registrationId);

        // 7. 새로운 Authentication 객체 생성
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                newPrincipalDetails,
                null,
                newPrincipalDetails.getAuthorities()
        );

        // 8. SecurityContext에 새 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        // 9. JWT 생성
        Token token = tokenService.generateTokens(newAuthentication);

        // 10. JWT 응답으로 보내기
        response.setHeader("Authorization", "Bearer " + token.getAccessToken());
    }

}
