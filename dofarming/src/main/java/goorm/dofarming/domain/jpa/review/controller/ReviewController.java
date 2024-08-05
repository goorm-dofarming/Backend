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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @Parameter @RequestBody ReviewCreateRequest request,
            @Parameter @RequestParam List<MultipartFile> files
    ) {
        return ResponseEntity.ok().body(reviewService.createReview(user.getUserId(), request, files));
    }

//    @GetMapping("")
//    public ResponseEntity<ReviewDTO> getReviewList(
//            @RequestParam("locationId") Long locationId,
//            @RequestParam("sortType") SortType sortType
//    ) {
//        return ResponseEntity.ok().body(reviewService.getReviews(locationId, sortType));
//    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @PathVariable("reviewId") Long reviewId,
            @Parameter @RequestBody ReviewModifyRequest request,
            @Parameter @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok().body(reviewService.updateReview(user.getUserId(), files, reviewId, request));
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
