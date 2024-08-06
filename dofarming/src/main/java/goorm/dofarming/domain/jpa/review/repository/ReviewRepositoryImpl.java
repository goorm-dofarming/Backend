package goorm.dofarming.domain.jpa.review.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.location.entity.QLocation;
import goorm.dofarming.domain.jpa.review.entity.QReview;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.user.entity.QUser;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static goorm.dofarming.domain.jpa.chatroom.entity.QChatroom.chatroom;
import static goorm.dofarming.domain.jpa.like.entity.QLike.like;
import static goorm.dofarming.domain.jpa.location.entity.QLocation.*;
import static goorm.dofarming.domain.jpa.message.entity.QMessage.message;
import static goorm.dofarming.domain.jpa.review.entity.QReview.*;
import static goorm.dofarming.domain.jpa.tag.entity.QTag.tag;
import static goorm.dofarming.domain.jpa.user.entity.QUser.*;
import static org.springframework.util.StringUtils.hasText;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> search(Long userId, Long locationId, Long reviewId, LocalDateTime createdAt, SortType sortType) {
        List<Long> reviewIds = queryFactory
                .select(review.reviewId)
                .from(review)
                .join(review.location, location)
                .where(
                        userIdNotEq(userId),
                        locationIdEq(locationId),
                        cursorCondition(reviewId, createdAt, sortType),
                        statusEq(Status.ACTIVE)
                )
                .orderBy(
                        sortOrder(sortType),
                        review.reviewId.desc()
                )
                .limit(5)
                .fetch();

        return queryFactory
                .select(review)
                .from(review)
                .join(review.user, user).fetchJoin()
                .where(
                        review.reviewId.in(reviewIds)
                )
                .orderBy(
                        sortOrder(sortType),
                        review.reviewId.desc()
                )
                .fetch();
    }

    private BooleanExpression userIdNotEq(Long userId) {
        return userId != null ? review.user.userId.eq(userId) : null;
    }

    private BooleanExpression locationIdEq(Long locationId) {
        return locationId != null ? review.location.locationId.eq(locationId) : null;
    }
    private BooleanExpression cursorCondition(Long reviewId, LocalDateTime createdAt, SortType sortType) {
        if (reviewId == null || createdAt == null) {
            return null;
        }

        if (sortType.equals(SortType.Earliest)) {
            return review.createdAt.after(createdAt)
                    .or(review.createdAt.eq(createdAt)).and(review.reviewId.gt(reviewId));
        }

        return review.createdAt.before(createdAt)
                .or(review.createdAt.eq(createdAt).and(review.reviewId.lt(reviewId)));
    }
    private BooleanExpression statusEq(Status status) {
        return hasText(status.name()) ? review.status.eq(status) : null;
    }
    private OrderSpecifier sortOrder(SortType sortType) {
        switch (sortType) {
            case Earliest:
                return review.createdAt.asc();
            case HighLike:
                return review.reviewLikeCount.desc();
            case LowLike:
                return review.reviewLikeCount.asc();
            case HighScore:
                return review.score.desc();
            case LowScore:
                return review.score.asc();
            default:
                return review.createdAt.desc(); // 기본 정렬
        }
    }
}
