package goorm.dofarming.domain.jpa.user.dto.request;

import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.global.common.error.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "회원 가입 요청 정보를 담는 DTO")
public record UserSignUpRequest(

        @Schema(description = "사용자 이메일", example = "user@example.com")
        @Email
        @NotEmpty(message = "이메일은 필수 항목입니다.")
        String email,

        @Schema(description = "사용자 비밀번호", example = "userpassword")
        @NotEmpty(message = "비밀번호는 필수 항목입니다.")
        String password,

        @Schema(description = "비밀번호 확인", example = "userpassword")
        @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
        String confirmPassword
) {
    public UserSignUpRequest {
        if (!password.equals(confirmPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH, "Password does not match.");
        }
    }
}
