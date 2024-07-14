package goorm.dofarming.global.auth;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attribute;

    public OAuth2UserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    // 소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "response"
    public abstract String getId();
    public abstract String getNickname();

    public abstract String getEmail();
    public abstract String getImageUrl();
}
