package goorm.dofarming.domain.jpa.auth.dto.request;

import java.util.Map;

public record OauthRequest(
        String socialType,
        Map<String, Object> data
) {
}
