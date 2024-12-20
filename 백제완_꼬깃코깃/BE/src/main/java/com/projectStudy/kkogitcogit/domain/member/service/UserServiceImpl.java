package com.projectStudy.kkogitcogit.domain.member.service;

import com.projectStudy.kkogitcogit.domain.member.dto.request.UserJoinRequest;
import com.projectStudy.kkogitcogit.domain.member.dto.response.UserJoinResponse;
import com.projectStudy.kkogitcogit.domain.member.entity.UserEntity;
import com.projectStudy.kkogitcogit.domain.member.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        repository.findByEmail(userJoinRequest.email()).ifPresent(user -> {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        });
        UserEntity userEntity = userJoinRequest.toEntity();
        repository.save(userEntity);
        return new UserJoinResponse(userEntity);
    }
}