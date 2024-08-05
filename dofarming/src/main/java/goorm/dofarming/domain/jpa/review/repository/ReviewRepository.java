package goorm.dofarming.domain.jpa.review.repository;

import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByLocation_LocationIdAndStatus(Long locationId, Status status);
}
