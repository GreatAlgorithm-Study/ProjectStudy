package com.example.talktalk_be.domain.member.controller;

import com.example.talktalk_be.domain.member.dto.request.SignUpRequestDto;
import com.example.talktalk_be.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws Exception {

        log.info(">> sing-up 컨트롤러 진입");
        memberService.signUp(signUpRequestDto);
        return ResponseEntity.ok().build();
    }
}
