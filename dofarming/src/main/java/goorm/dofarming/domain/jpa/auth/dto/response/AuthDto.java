package goorm.dofarming.domain.jpa.auth.dto.response;

import goorm.dofarming.domain.jpa.user.entity.Role;
import lombok.Builder;

@Builder
public record AuthDto(
        Long userId,
        String email,
        String nickname,
        String password,
        Role role
) {
}
