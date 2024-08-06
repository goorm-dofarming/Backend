package goorm.dofarming.domain.jpa.like.controller;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import goorm.dofarming.domain.jpa.like.dto.response.LikeResponse;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.like.service.LikeService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Like", description = "Like 관련 API")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(
            operationId = "like",
            summary = "좋아요 추가 또는 취소 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Like.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PostMapping("/like")
    public void like(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "장소 ID", required = true) @RequestParam Long locationId
    ) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();

        likeService.like(userId, locationId);
    }

    @Operation(
            operationId = "likeList",
            summary = "사용자의 좋아요 목록 조회 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class),
                                    examples = @ExampleObject(
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"likeId\": 1,\n" +
                                                    "    \"updatedAt\": \"2023-07-21T12:34:56\",\n" +
                                                    "    \"locationResponse\": {\n" +
                                                    "      \"id\": 1,\n" +
                                                    "      \"type\": \"Restaurant\",\n" +
                                                    "      \"title\": \"용용 선생\",\n" +
                                                    "      \"addr\": \"서울시 강남구\",\n" +
                                                    "      \"tel\": \"010-1234-5678\",\n" +
                                                    "      \"image\": \"http://example.com/image.jpg\",\n" +
                                                    "      \"mapX\": 127.123456,\n" +
                                                    "      \"mapY\": 37.123456,\n" +
                                                    "      \"likeCount\": 7\n" +
                                                    "    }\n" +
                                                    "  }\n" + "]")
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/likeList")
    private List<LikeResponse> likeList(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "like ID") @RequestParam(required = false) Long likeId,
            @Parameter(description = "생성 일자") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAt,
            @Parameter(description = "제목 검색") @RequestParam(required = false) String title,
            @Parameter(description = "테마 종류") @RequestParam(required = false) List<String> themes,
            @Parameter(description = "지역 이름") @RequestParam(required = false) List<Region> regions,
            @Parameter(description = "정렬 순서") @RequestParam SortType sortType
    ) {
        return likeService.getLikeList(user.getUserId(), likeId, updatedAt, title, themes, regions, sortType);
    }
}
