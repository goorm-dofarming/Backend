package goorm.dofarming.domain.jpa.review.repository;

import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Optional<Review> findByLocation_LocationIdAndUser_UserIdAndStatus(Long locationId, Long userId, Status status);

    Optional<Review> findReviewByReviewIdAndStatus(Long id, Status status);

    Boolean existsByLocation_LocationIdAndUser_UserIdAndStatus(Long locationId, Long userId, Status status);
}
