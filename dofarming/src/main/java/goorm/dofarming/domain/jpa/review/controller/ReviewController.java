package goorm.dofarming.domain.jpa.review.controller;

import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.review.dto.ReviewDTO;
import goorm.dofarming.domain.jpa.review.dto.ReviewResponse;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewCreateRequest;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewModifyRequest;
import goorm.dofarming.domain.jpa.review.service.ReviewService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    // body에서 form-data 로 받아옴. score와 content는 text로 받음.
    @PostMapping("")
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @RequestPart ReviewCreateRequest reviewCreateRequest,
            @Parameter @RequestPart(required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok().body(reviewService.createReview(user.getUserId(), reviewCreateRequest, files));
    }

    @GetMapping("")
    public ResponseEntity<List<ReviewResponse>> getReviewList(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @RequestParam(required = false) Boolean myReview,
            @Parameter @RequestParam(required = false) Long locationId,
            @Parameter @RequestParam(required = false) Long reviewId,
            @Parameter @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime createdAt,
            @Parameter @RequestParam SortType sortType
    ) {
        return ResponseEntity.ok().body(reviewService.getReviews(user.getUserId(), myReview, locationId, reviewId, createdAt, sortType));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @PathVariable("reviewId") Long reviewId,
            @Parameter @RequestPart ReviewModifyRequest reviewModifyRequest,
            @Parameter @RequestPart(required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok().body(reviewService.updateReview(user.getUserId(), files, reviewId, reviewModifyRequest));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @PathVariable("reviewId") Long reviewId
    ) {
        reviewService.deleteReview(user.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
