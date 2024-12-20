package com.projectStudy.kkogitcogit.domain.member.controller;

import com.projectStudy.kkogitcogit.domain.member.dto.request.UserJoinRequest;
import com.projectStudy.kkogitcogit.domain.member.dto.response.UserJoinResponse;
import com.projectStudy.kkogitcogit.domain.member.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/join")
    public UserJoinResponse join(@RequestBody UserJoinRequest userJoinRequest) {
        log.info("회원 가입 요청: {}", userJoinRequest);
        return userService.join(userJoinRequest);
    }
}
