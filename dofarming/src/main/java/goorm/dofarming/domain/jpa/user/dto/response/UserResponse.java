package goorm.dofarming.domain.jpa.user.dto.response;

import goorm.dofarming.domain.jpa.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원 응답 정보를 담는 DTO")
public record UserResponse(

        @Schema(description = "사용자 Id", example = "1")
        Long userId,

        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @Schema(description = "사용자 이미지 URL", example = "http://example.com/image.jpg")
        String imageUrl,

        @Schema(description = "사용자 닉네임", example = "usernickname")
        String nickname
) {
        public static UserResponse of(User user) {
                return new UserResponse(
                        user.getUserId(),
                        user.getEmail(),
                        user.getImageUrl(),
                        user.getNickname()
                );
        }
}
