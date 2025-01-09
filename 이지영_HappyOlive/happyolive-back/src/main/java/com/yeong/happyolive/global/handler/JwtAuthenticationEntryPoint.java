package com.yeong.happyolive.global.handler;

import com.yeong.happyolive.global.utils.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.info("JwtException Error 발생 : "+authException.getMessage());
        // 예외 정보 가져오기
        Integer errorCode = (Integer) request.getAttribute("exceptionCode");
        String errorMessage = (String) request.getAttribute("exceptionMessage");
        log.info(">> code : " + errorCode);
        log.info(">> message : " + errorMessage);

        if(errorMessage.equals(ErrorCode.NO_VALUE_PRESENT.getMessage())){
            // 토큰이 없을 때
            setResponse(response, ErrorCode.NO_VALUE_PRESENT);
        } else if(errorMessage.equals(ErrorCode.BLACKLISTED_TOKENS.getMessage())) {
            // 블랙리스트 등록된 토큰일 때
            setResponse(response, ErrorCode.BLACKLISTED_TOKENS);
        } else if(errorMessage.equals(ErrorCode.MISSING_PARTS.getMessage())) {
            // 토큰의 일부가 사라졌을 때
            setResponse(response, ErrorCode.MISSING_PARTS);
        } else if(errorMessage.contains(ErrorCode.EXPIRED_TOKEN.getMessage())){
            // 만료된 토큰일 때
            setResponse(response, ErrorCode.EXPIRED_TOKEN);
        } else {
            // 그 외
            setResponse(response, ErrorCode.UNKNOWN_ERROR);
        }

    }

    private void setResponse(HttpServletResponse response, ErrorCode errorMessage) throws RuntimeException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getCode());
        response.getWriter().print(errorMessage.getMessage());
//        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
    }
}
