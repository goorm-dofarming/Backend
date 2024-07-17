package goorm.dofarming.domain.jpa.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "로그인 요청 정보를 담는 DTO")
public record SignInRequest(

        @Schema(description = "사용자 이메일", example = "user@example.com")
        @Email
        @NotEmpty(message = "이메일은 필수 항목입니다.")
        String email,

        @Schema(description = "사용자 비밀번호", example = "userpassword")
        @NotEmpty(message = "비밀번호는 필수 항목입니다.")
        String password
) {
}
