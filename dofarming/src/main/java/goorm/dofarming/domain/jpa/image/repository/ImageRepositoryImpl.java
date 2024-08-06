package goorm.dofarming.domain.jpa.image.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.entity.QImage;
import goorm.dofarming.domain.jpa.review.entity.QReview;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static goorm.dofarming.domain.jpa.image.entity.QImage.*;
import static goorm.dofarming.domain.jpa.review.entity.QReview.*;

public class ImageRepositoryImpl implements ImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ImageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Image> findTopImageByReviewLike(Long locationId) {
        Image getTopImage = queryFactory
                .select(image)
                .from(image)
                .join(image.review, review).fetchJoin()
                .where(
                        review.location.locationId.eq(locationId),
                        image.status.eq(Status.ACTIVE),
                        review.status.eq(Status.ACTIVE)
                )
                .orderBy(
                        review.reviewLikeCount.desc(),
                        image.createdAt.desc()
                )
                .fetchFirst();

        return Optional.ofNullable(getTopImage);
    }
}
