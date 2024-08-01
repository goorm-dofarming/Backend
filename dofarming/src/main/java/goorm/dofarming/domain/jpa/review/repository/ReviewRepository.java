package goorm.dofarming.domain.jpa.review.repository;

import goorm.dofarming.domain.jpa.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
