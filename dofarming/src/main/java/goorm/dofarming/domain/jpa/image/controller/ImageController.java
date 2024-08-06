package goorm.dofarming.domain.jpa.image.controller;

import goorm.dofarming.domain.jpa.image.service.ImageService;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Image", description = "Image 관련 API")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(
            operationId = "deleteImage",
            summary = "이미지 삭제 API",
            responses = {
                    @ApiResponse(responseCode = "204", description = "이미지가 성공적으로 삭제되었습니다.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없습니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )

    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @Parameter(description = "삭제할 이미지의 ID", required = true) @PathVariable Long imageId
    ) {
        imageService.delete(imageId);
        return ResponseEntity.noContent().build();
    }
}
