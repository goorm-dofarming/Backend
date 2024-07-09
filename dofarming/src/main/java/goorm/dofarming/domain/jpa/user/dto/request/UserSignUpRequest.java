package goorm.dofarming.domain.jpa.user.dto.request;

import goorm.dofarming.global.common.exception.CustomException;
import goorm.dofarming.global.common.exception.ErrorCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserSignUpRequest(

        @Email
        @NotEmpty(message = "이메일은 필수 항목입니다.")
        String email,

        @NotEmpty(message = "비밀번호는 필수 항목입니다.")
        String password,

        @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
        String confirmPassword
) {
    public UserSignUpRequest {
        if (!password.equals(confirmPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
