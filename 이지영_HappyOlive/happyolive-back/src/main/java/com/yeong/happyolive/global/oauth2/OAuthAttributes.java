package com.yeong.happyolive.global.oauth2;

import com.yeong.happyolive.auth.domain.SocialType;
import com.yeong.happyolive.auth.domain.User;
import com.yeong.happyolive.global.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.yeong.happyolive.global.oauth2.userInfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import jakarta.transaction.Transactional;

import java.util.Map;

@Getter
@Slf4j
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {
        log.info("구글 소셜로그인 attribute");

        return ofGoogle(userNameAttributeName, attributes);
    }


    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }


    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 USER로 설정
     */
    @Transactional
    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return new User(oauth2UserInfo.getNickname(), oauth2UserInfo.getEmail(), socialType, oauth2UserInfo.getId());
    }
}
