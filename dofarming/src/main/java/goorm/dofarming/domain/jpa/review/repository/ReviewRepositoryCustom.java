package goorm.dofarming.domain.jpa.review.repository;

import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.review.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> search(Long locationId, Long reviewId, LocalDateTime createdAt, SortType sortType);
}
