package goorm.dofarming.domain.jpa.image.repository;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}