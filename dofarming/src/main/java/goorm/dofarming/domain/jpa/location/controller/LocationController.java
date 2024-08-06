package goorm.dofarming.domain.jpa.location.controller;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.service.LocationService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Location", description = "Location 관련 API")
@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    @Operation(
            operationId = "getLocation",
            summary = "특정 장소 정보 조회 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LocationResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "장소를 찾을 수 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )

    @GetMapping("/location/{locationId}")
    public ResponseEntity<LocationResponse> getLocation(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "장소ID", required = true) @PathVariable Long locationId
    ) {
        return ResponseEntity.ok().body(locationService.getLocation(user.getUserId(), locationId));
    }
}
