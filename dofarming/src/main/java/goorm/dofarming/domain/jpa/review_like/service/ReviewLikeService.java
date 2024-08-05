package goorm.dofarming.domain.jpa.review_like.service;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.review.repository.ReviewRepository;
import goorm.dofarming.domain.jpa.review_like.entity.ReviewLike;
import goorm.dofarming.domain.jpa.review_like.repository.ReviewLikeRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public void reviewLike(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        Review review = reviewRepository.findReviewByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));

        reviewLikeRepository.findByUser_UserIdAndReview_ReviewId(userId, reviewId)
                .ifPresentOrElse(
                        reviewLike -> {
                            if (reviewLike.getStatus().equals(Status.ACTIVE)) {
                                reviewLike.delete();
                            } else {
                                reviewLike.active();
                            }
                        },
                        () -> reviewLikeRepository.save(ReviewLike.reviewLike(user, review))
                );
    }
}
