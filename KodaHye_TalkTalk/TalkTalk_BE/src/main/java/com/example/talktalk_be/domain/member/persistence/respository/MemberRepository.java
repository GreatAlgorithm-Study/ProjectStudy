package com.example.talktalk_be.domain.member.persistence.respository;

import com.example.talktalk_be.domain.member.persistence.entity.Member;
import com.example.talktalk_be.domain.member.persistence.entity.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, Long socialId);
    Optional<Member> findByEmail(String email);
    Optional<Member>  findByNickname(String nickname);
    Optional<Member> findByRefreshToken(String refreshToken);
}
