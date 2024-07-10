package goorm.dofarming.domain.jpa.user.controller;

import goorm.dofarming.domain.jpa.user.dto.request.UserModifyRequest;
import goorm.dofarming.domain.jpa.user.dto.request.UserSignUpRequest;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.domain.jpa.user.service.UserService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            operationId = "User",
            summary = "회원 가입 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "클라이언트의 요청을 서버가 정상적으로 처리했고 새로운 리소스가 생겼다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(
            @Valid
            @Parameter
            @RequestBody UserSignUpRequest signUpRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(signUpRequest));
    }


    @Operation(
            operationId = "User",
            summary = "회원 수정 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "클라이언트의 요청을 서버가 정상적으로 처리했다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PutMapping("/users")
    public ResponseEntity<UserResponse> modify(
            @Valid
            @Parameter
            @RequestBody UserModifyRequest modifyRequest
    ) {
        return ResponseEntity.ok().body(userService.updateUser(modifyRequest));
    }


    @Operation(
            operationId = "User",
            summary = "회원 삭제 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "클라이언트의 요청은 정상적이다. 하지만 컨텐츠를 제공하지 않는다.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> delete(
            @Valid
            @Parameter
            @PathVariable Long userId
    ) {
        userService.userDelete(userId);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            operationId = "User",
            summary = "회원 조회 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "클라이언트의 요청을 서버가 정상적으로 처리했다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @Valid
            @Parameter
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }
}
