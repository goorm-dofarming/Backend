package goorm.dofarming.domain.jpa.review.repository;

import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Status 가 ALIVE 인 경우만 리뷰를 반환해야 할 수도 있음.
    List<Review> findAllByStatus(Status status);
}
