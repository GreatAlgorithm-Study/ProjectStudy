package com.example.talktalk_be.domain.member.service;
import com.example.talktalk_be.domain.member.dto.request.SignUpRequestDto;
import com.example.talktalk_be.domain.member.persistence.entity.Member;
import com.example.talktalk_be.domain.member.persistence.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDto request) throws Exception {
        if(memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(memberRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .name(request.getName())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build();

        member.encodePassword(passwordEncoder);

        memberRepository.save(member);
    }
}
