package com.yeong.happyolive.auth.controller;

import com.yeong.happyolive.auth.service.UserService;
import com.yeong.happyolive.global.jwt.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/jwtTest")
    public ResponseEntity<?> jwtTest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("jwtTest 컨트롤러 진입 ===================== ");
        return new ResponseEntity<>("Sended JWT successfully", HttpStatus.OK);
    }


    /**
     * 로그아웃
     * */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("로그아웃 컨트롤러 진입 ===================== ");

        log.info("isError ? "+ request.getAttribute("exception"));
        HttpStatus status = (HttpStatus) request.getAttribute("exception");
        if (status == null) {
            status = HttpStatus.OK;
        }
        if (!status.is2xxSuccessful()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(() -> new NullPointerException(""));
        System.out.println("헤더의 ATK : "+ accessToken);

        userService.logout(accessToken);


        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    /**
     * 로그인 연장
     * */
    @GetMapping("/reissue")
    public ResponseEntity refreshLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("로그인 연장 컨틀롤러 진입================== ");

        log.info("isError ? "+ request.getAttribute("exception"));
        HttpStatus status = (HttpStatus) request.getAttribute("exception");
        String atk = response.getHeader("Authorization");
        String rtk = response.getHeader("Authorization-refresh");
        log.info("새로 발급된 엑세스 토큰 : "+atk);
        log.info("새로 발급된 리프레시 토큰 : "+rtk);

        if (status == null) {
            status = HttpStatus.OK;
        }
        if (!status.is2xxSuccessful()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization",
                response.getHeader("Authorization"));
        responseHeaders.set("Authorization-refresh", response.getHeader("Authorization-refresh"));

        return ResponseEntity.ok()
//                .headers(responseHeaders)
                .body("Response with header using ResponseEntity");
    }

}
