package goorm.dofarming.global.auth;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attribute) {
        super(attribute);
    }

    @Override
    public String getId() {
        return String.valueOf(attribute.get("id"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");

        if (account == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");

        if (account == null) {
            return null;
        }

        return (String) account.get("email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");

        if (account == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) {
            return null;
        }

        return (String) profile.get("thumbnail_image_url");
    }
}
