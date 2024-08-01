package goorm.dofarming.domain.jpa.log.controller;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.log.dto.response.LogResponse;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.domain.jpa.log.service.LogService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.ErrorResponse;
import goorm.dofarming.global.common.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "Log", description = "Log 관련 API")
@RestController
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(
            operationId = "getLogsByUserId",
            summary = "사용자의 로그를 조회하는 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Log.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/getLogs")
    public List<LogResponse> getLogsByUserId(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return logService.getLogsByUserId(userId);
    }

    @Operation(
            operationId = "getLogData",
            summary = "특정 로그의 데이터를 조회하는 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class),
                                    examples = @ExampleObject(
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 73,\n" +
                                                    "    \"title\": \"남해 가천 해변과 암수바위\",\n" +
                                                    "    \"addr\": \"경상남도 남해군 남면\",\n" +
                                                    "    \"tel\": \"\",\n" +
                                                    "    \"image\": \"\",\n" +
                                                    "    \"mapX\": 127.8940102692,\n" +
                                                    "    \"mapY\": 34.72648889,\n" +
                                                    "    \"dataType\": 1,\n" +
                                                    "    \"liked\": true,\n" +
                                                    "    \"countLikes\": 1\n" +
                                                    "  }\n" + "]")
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "로그를 찾을 수 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/getLogData")
    public List<LocationResponse> getLogData(
            @Parameter(description = "로그 ID") Long logId
    ) {
        return logService.getLogData(logId);
    }
}
