package goorm.dofarming.domain.jpa.join.controller;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomCreateRequest;
import goorm.dofarming.domain.jpa.join.dto.request.WatermarkRequest;
import goorm.dofarming.domain.jpa.join.service.JoinService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Join", description = "Join 관련 API")
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @Operation(
            operationId = "Join",
            summary = "읽은 메시지 갱신 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "클라이언트의 요청을 서버가 정상적으로 처리했고 새로운 리소스가 생겼다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
                    })
            }
    )
    @PutMapping("/join")
    public ResponseEntity<Void> updateWatermark(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @Valid @RequestBody WatermarkRequest watermarkRequest
    ) {
        joinService.updateWatermark(user.getUserId(), watermarkRequest);
        return ResponseEntity.noContent().build();
    }
}
