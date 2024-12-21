package com.projectStudy.kkogitcogit.domain.user.service;

import com.projectStudy.kkogitcogit.domain.user.dto.request.UserJoinRequest;
import com.projectStudy.kkogitcogit.domain.user.dto.response.UserJoinResponse;
import com.projectStudy.kkogitcogit.domain.user.entity.UserEntity;
import com.projectStudy.kkogitcogit.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        repository.findByEmail(userJoinRequest.email()).ifPresent(user -> {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        });
        UserEntity userEntity = userJoinRequest.toEntity(bCryptPasswordEncoder);
        repository.save(userEntity);
        return new UserJoinResponse(userEntity);
    }
}