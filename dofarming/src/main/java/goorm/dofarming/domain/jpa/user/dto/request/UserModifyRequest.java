package goorm.dofarming.domain.jpa.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "회원 수정 요청 정보를 담는 DTO")
public record UserModifyRequest(

        @Schema(description = "사용자 닉네임", example = "usernickname")
        @NotEmpty(message = "닉네임은 필수 항목입니다.")
        String nickname,

        @Schema(description = "사용자 비밀번호", example = "userpassword")
        @NotEmpty(message = "비밀번호는 필수 항목입니다.")
        String password
) {
}
