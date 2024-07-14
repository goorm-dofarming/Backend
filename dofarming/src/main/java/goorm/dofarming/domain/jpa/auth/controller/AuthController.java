package goorm.dofarming.domain.jpa.auth.controller;

import goorm.dofarming.domain.jpa.auth.dto.request.OauthRequest;
import goorm.dofarming.domain.jpa.auth.dto.request.SignInRequest;
import goorm.dofarming.domain.jpa.auth.service.AuthService;
import goorm.dofarming.domain.jpa.user.dto.request.UserSignUpRequest;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.global.common.error.ErrorResponse;
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

import java.util.Map;

@Tag(name = "Auth", description = "Auth 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            operationId = "Auth",
            summary = "일반 로그인 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "클라이언트의 요청을 서버가 정상적으로 처리했다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    }),
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid
            @Parameter
            @RequestBody SignInRequest signInRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(signInRequest));
    }


    @Operation(
            operationId = "Auth",
            summary = "OAuth2 로그인 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "클라이언트의 요청을 서버가 정상적으로 처리했다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    }),
            }
    )
    @PostMapping("/oauth")
    public ResponseEntity<String> oauthLogin(
            @Parameter @RequestBody OauthRequest oauthRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.oauthLogin(oauthRequest));
    }
}
