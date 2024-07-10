package goorm.dofarming.domain.jpa.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EmailCheckRequest(
        @Email
        @NotEmpty(message = "이메일은 필수 항목입니다.")
        String email,

        @NotEmpty(message = "이메일 인증번호는 필수 항목입니다.")
        String emailNumber
) {
}
