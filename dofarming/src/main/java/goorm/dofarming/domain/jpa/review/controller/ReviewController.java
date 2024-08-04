package goorm.dofarming.domain.jpa.review.controller;

import goorm.dofarming.domain.jpa.review.dto.ReviewDTO;
import goorm.dofarming.domain.jpa.review.dto.ReviewResponse;
import goorm.dofarming.domain.jpa.review.service.ReviewService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
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
            @RequestParam("score") Double score,
            @RequestParam("content") String content,
            @RequestParam("locationId") Long locationId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return ResponseEntity.ok().body(reviewService.createReview(files, userId, locationId, score, content));
    }

    @GetMapping("")
    public ResponseEntity<List<ReviewResponse>> getReviewList(
            @RequestParam("locationId") Long locationId
    ) {
        return ResponseEntity.ok().body(reviewService.getReviews(locationId));
    }

    @GetMapping("/test")
    public ResponseEntity<ReviewDTO> getReviewListTest(
            @RequestParam("locationId") Long locationId
    ) {
        return ResponseEntity.ok().body(reviewService.getReviewsTest(locationId));
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> getImages(
            @RequestParam("reviewId") Long reviewId
    ) {
        return ResponseEntity.ok().body(reviewService.getImageUrls(reviewId));
    }
}
