package com.example.talktalk_be.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class KakaoMemberInfoResponseDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
    @JsonProperty("properties")
    private JsonNode properties;
//    private String nickname;
//    private String email;

    @Getter
    @AllArgsConstructor
    public static class KakaoAccount {
        @JsonProperty("profile_nickname_needs_agreement")
        private Boolean profileNicknameNeedsAgreement;
        private Profile profile;
        @JsonProperty("is_email_valid")
        private Boolean isEmailValid;
        @JsonProperty("is_email_verified")
        private Boolean isEmailVerified;
        private String  email;
    }

    @Getter
    @AllArgsConstructor
    public static class Profile{
        private String nickname;
        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }
}
