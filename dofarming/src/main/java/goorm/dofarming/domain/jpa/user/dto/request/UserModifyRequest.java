package goorm.dofarming.domain.jpa.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserModifyRequest(

        @NotEmpty(message = "유저 ID는 필수 항목입니다.")
        Long userId,

        @NotEmpty(message = "닉네임은 필수 항목입니다.")
        String nickname,

        @NotEmpty(message = "비밀번호는 필수 항목입니다.")
        String password
) {
}
