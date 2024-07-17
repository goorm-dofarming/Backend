package goorm.dofarming.domain.jpa.email.controller;

import goorm.dofarming.domain.jpa.auth.dto.request.SignInRequest;
import goorm.dofarming.domain.jpa.email.dto.request.EmailCheckRequest;
import goorm.dofarming.domain.jpa.email.dto.request.EmailRequest;
import goorm.dofarming.domain.jpa.email.service.EmailService;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email", description = "Email 관련 API")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(
            operationId = "Email",
            summary = "인증번호 전송 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "클라이언트의 요청은 정상적이다. 하지만 컨텐츠를 제공하지 않는다.", content = @Content),
            }
    )
    @PostMapping("/email/send")
    public ResponseEntity<String> mailSend(
            @Valid
            @Parameter
            @RequestBody EmailRequest emailRequest
    ) {
        emailService.joinEmail(emailRequest.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            operationId = "Email",
            summary = "인증번호 확인 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "클라이언트의 요청은 정상적이다. 하지만 컨텐츠를 제공하지 않는다.", content = @Content),
            }
    )
    @PostMapping("/email/check")
    public ResponseEntity<String> mailCheck(
            @Valid
            @Parameter
            @RequestBody EmailCheckRequest emailCheckRequest
            ) {
        boolean isCheck = emailService.checkEmailNumber(emailCheckRequest.email(), emailCheckRequest.emailNumber());
        if (isCheck) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "맞는 인증번호가 아닙니다.");
        }
    }
}
