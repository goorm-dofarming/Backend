package goorm.dofarming.domain.jpa.image.repository;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.global.common.entity.Status;

import java.util.Optional;

public interface ImageRepositoryCustom {
    Optional<Image> findTopImageByReviewLike(Long locationId);
}
