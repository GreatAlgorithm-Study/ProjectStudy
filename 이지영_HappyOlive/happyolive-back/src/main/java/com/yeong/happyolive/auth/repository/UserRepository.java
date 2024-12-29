package com.yeong.happyolive.auth.repository;

import com.yeong.happyolive.auth.domain.SocialType;
import com.yeong.happyolive.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * 사용자 UUID로 찾기
     * */
    User findByUuidAndIsDeleted(UUID uuid, int isDeleted);

    /**
     * 사용자 email로 찾기
     * */
    Optional<User> findByEmailAndIsDeleted(String email, int isDeleted);

    /**
     * 소셜 타입과 소셜의 식별값으로 회원 찾는 메소드
     * 따라서 추가 정보를 입력받아 회원 가입을 진행할 때 소셜 타입, 식별자로 해당 회원을 찾기 위한 메소드
     */
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}