package goorm.dofarming.domain.jpa.image.repository;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByReviewAndStatus(Review review, Status status);

    Optional<Image> findByImageIdAndStatus(Long imageId, Status status);
}