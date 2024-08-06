package goorm.dofarming.domain.jpa.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "이메일 요청 정보를 담는 DTO")
public record EmailRequest(

        @Schema(description = "사용자 이메일", example = "user@example.com")
        @Email
        @NotEmpty(message = "이메일은 필수 항목입니다.")
        String email
) {
}
