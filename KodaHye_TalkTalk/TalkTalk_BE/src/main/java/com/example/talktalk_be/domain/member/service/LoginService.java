package com.example.talktalk_be.domain.member.service;

import com.example.talktalk_be.domain.member.persistence.entity.Member;
import com.example.talktalk_be.domain.member.persistence.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
UserDetailsService를 상속받아 UserDetails를 반환하는 클래스
 */

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 갖는 유저가 없습니다."));

        return User.withUsername(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
