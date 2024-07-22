package goorm.dofarming.domain.jpa.auth.dto.response;

import goorm.dofarming.domain.jpa.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "내 정보 응답 정보를 담는 DTO")
public record MyInfoResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @Schema(description = "사용자 닉네임", example = "usernickname")
        String nickname,
        @Schema(description = "사용자 이미지 URL", example = "http://example.com/image.jpg")
        String imageUrl,

        @Schema(description = "사용자 역할", example = "DOFARMING")
        Role role
) {
}
