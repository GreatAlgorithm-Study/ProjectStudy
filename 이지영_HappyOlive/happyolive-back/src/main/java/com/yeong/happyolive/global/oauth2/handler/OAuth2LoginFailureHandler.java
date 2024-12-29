package com.yeong.happyolive.global.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    //    private final CookieAuthorizationRequestRepository authorizationRequestRepository;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("소셜 로그인 실패! 서버 로그를 확인해주세요.");
        log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
        response.sendRedirect("/");

//        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//                .map(Cookie::getValue).orElse("/");
//
//        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("Error", exception.getLocalizedMessage())
//                .build().toUriString();
//
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
