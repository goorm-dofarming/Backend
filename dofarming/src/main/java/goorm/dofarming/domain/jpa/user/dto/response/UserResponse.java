package goorm.dofarming.domain.jpa.user.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long userId,

        String email,

        String imageUrl,

        String nickname
) {
}
