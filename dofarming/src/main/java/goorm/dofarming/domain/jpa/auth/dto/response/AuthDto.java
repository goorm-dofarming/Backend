package goorm.dofarming.domain.jpa.auth.dto.response;

import goorm.dofarming.domain.jpa.user.entity.Role;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.auth.GoogleOAuth2UserInfo;
import lombok.Builder;

import java.util.Map;

@Builder
public record AuthDto(
        Long userId,
        String email,
        String nickname,
        String imageUrl,
        String password,
        Role role
) {

    public static AuthDto from(User user) {
        return new AuthDto(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getImageUrl(),
                user.getPassword(),
                user.getRole()
        );
    }
}
