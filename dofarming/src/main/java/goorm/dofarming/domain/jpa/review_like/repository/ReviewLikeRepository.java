package goorm.dofarming.domain.jpa.review_like.repository;

import goorm.dofarming.domain.jpa.review_like.entity.ReviewLike;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByUser_UserIdAndReview_ReviewId(Long userId, Long reviewId);

    boolean existsByReview_ReviewIdAndUser_UserIdAndStatus(Long reviewId, Long userId, Status status);
}
