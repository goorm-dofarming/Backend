package goorm.dofarming.domain.jpa.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "소셜 로그인 요청 정보를 담는 DTO")
public record OauthRequest(

        @Schema(description = "소셜 타입", example = "kakao")
        String socialType,

        @Schema(description = "소셜 로그인에 필요한 추가 데이터", example = "{\"accessToken\": \"abc123\", \"userId\": \"user123\"}")
        Map<String, Object> data
) {
}
