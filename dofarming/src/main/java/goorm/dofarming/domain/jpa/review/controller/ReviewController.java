package goorm.dofarming.domain.jpa.review.controller;

import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.review.dto.ReviewResponse;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewCreateRequest;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewModifyRequest;
import goorm.dofarming.domain.jpa.review.service.ReviewService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Review", description = "Review 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            operationId = "createReview",
            summary = "리뷰 생성 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 생성됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 또는 장소", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PostMapping("")
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "리뷰 생성 요청 데이터") @RequestPart ReviewCreateRequest reviewCreateRequest,
            @Parameter(description = "이미지 파일 리스트") @RequestPart(required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok().body(reviewService.createReview(user.getUserId(), reviewCreateRequest, files));
    }

    @Operation(
            operationId = "getReviewList",
            summary = "리뷰 목록 조회 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저 또는 장소", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("")
    public ResponseEntity<List<ReviewResponse>> getReviewList(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "내 리뷰 여부") @RequestParam(required = false) Boolean myReview,
            @Parameter(description = "장소 ID") @RequestParam(required = false) Long locationId,
            @Parameter(description = "리뷰 ID") @RequestParam(required = false) Long reviewId,
            @Parameter(description = "리뷰 생성 날짜") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
            @Parameter(description = "정렬 타입") @RequestParam SortType sortType
    ) {
        return ResponseEntity.ok().body(reviewService.getReviews(user.getUserId(), myReview, locationId, reviewId, createdAt, sortType));
    }

    @Operation(
            operationId = "updateReview",
            summary = "리뷰 수정 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 수정됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "리뷰 수정 권한 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저 또는 리뷰", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "리뷰 ID") @PathVariable("reviewId") Long reviewId,
            @Parameter(description = "리뷰 수정 요청 데이터") @RequestPart ReviewModifyRequest reviewModifyRequest,
            @Parameter(description = "이미지 파일 리스트") @RequestPart(required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok().body(reviewService.updateReview(user.getUserId(), files, reviewId, reviewModifyRequest));
    }

    @Operation(
            operationId = "deleteReview",
            summary = "리뷰 삭제 API",
            responses = {
                    @ApiResponse(responseCode = "204", description = "리뷰가 성공적으로 삭제됨", content = @Content),
                    @ApiResponse(responseCode = "403", description = "리뷰 삭제 권한 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 리뷰", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "리뷰 ID") @PathVariable("reviewId") Long reviewId
    ) {
        reviewService.deleteReview(user.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
