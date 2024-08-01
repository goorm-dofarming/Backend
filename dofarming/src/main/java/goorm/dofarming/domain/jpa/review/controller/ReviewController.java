package goorm.dofarming.domain.jpa.review.controller;

import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.review.entity.ReviewDTO;
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

    @PostMapping("")
    public ResponseEntity<ReviewDTO> createReview(
            @RequestParam("score") Double score,
            @RequestParam("content") String content,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return ResponseEntity.ok().body(reviewService.saveReview(files, userId, score, content));
    }

    @GetMapping("/images")
    public List<String> getImages(
            @RequestParam("reviewId") Long reviewId
    ) {
        return reviewService.getImageUrls(reviewId);
    }
}
