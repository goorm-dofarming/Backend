package goorm.dofarming.domain.jpa.auth.dto.response;

import goorm.dofarming.domain.jpa.user.entity.Role;
import lombok.Builder;

@Builder
public record MyInfoResponse(
        Long userId,
        String email,
        String nickname,
        String imageUrl,
        Role role
) {
}
