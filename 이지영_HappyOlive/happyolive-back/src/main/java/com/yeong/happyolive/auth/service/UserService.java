package com.yeong.happyolive.auth.service;

import com.yeong.happyolive.auth.domain.SocialType;
import com.yeong.happyolive.auth.domain.User;
import com.yeong.happyolive.auth.repository.UserRepository;
import com.yeong.happyolive.global.jwt.service.JwtService;
import com.yeong.happyolive.global.utils.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 현재 로그인한 유저의 ATK를 받아서
     * Redis에 저장하고
     * SecurityContextHolder에서 삭제
     */
    public void logout(String accessToken) {

        // 남은 유효시간 계산
        Optional<Date> expiration = jwtService.extractExpiration(accessToken);
        Long curTime = new Date().getTime();
        Long remainTime = expiration.get().getTime() - curTime;

        // email 얻기
        String email = jwtService.extractEmail(accessToken).orElseThrow(() -> new NullPointerException(""));
        // myUser 얻기
        User myUser = userRepository.findByEmailAndIsDeleted(email, 0).orElseThrow(() -> new NullPointerException(""));
        // password 생성
        String password = PasswordUtil.generateRandomPassword();


        //Redis Cache에 저장
        redisTemplate.opsForValue().set(accessToken, "logout", remainTime, TimeUnit.MILLISECONDS);

        //리프레쉬 토큰 삭제
        redisTemplate.delete(email);


        SecurityContext context = SecurityContextHolder.getContext();
        log.info("============logout context : " + context);
        // 인증 객체 삭제
        context.setAuthentication(null);
        // Security Context 삭제
        SecurityContextHolder.clearContext();
        log.info("============ after context : " + SecurityContextHolder.getContext());

    }

    public void signUp() {
        User createUser = new User("lee", "lee@test.com", SocialType.GOOGLE, "1111");
        userRepository.save(createUser);
    }

    // @TODO 유저 관련 업데이트 함수 작성
//    public UserResponseDto getUserById(Long userId) {
//        User user = userRepository.findByUserId(userId);
//
//        return new UserResponseDto(user);
//    }
//
//    public User getAdminUser(Long adminUserId) {
//        User adminUser = userRepository.findByUserId(adminUserId);
//        return adminUser;
//    }
//
//    public void updateUser(Long userId, UserUpdateDto userUpdateDto) {
//        User user = userRepository.findByUserId(userId);
//
//        if (user == null) {
//            throw new EntityNotFoundException("User not found with id: " + userId);
//        }
//
//        if (userUpdateDto.getHometown() != null) {
//            user.setHometown(userUpdateDto.getHometown());
//        }
//
//        if (userUpdateDto.getPhone() != null) {
//            user.setPhone(userUpdateDto.getPhone());
//        }
//    }
//
//    public void deleteUser(Long userId) {
//        User user = userRepository.findByUserId(userId);
//
//        if (user == null) {
//            throw new EntityNotFoundException("User not found with id: " + userId);
//        }
//
//        user.setDeleted(true); // 유저를 삭제 상태로 표시
//    }

}
