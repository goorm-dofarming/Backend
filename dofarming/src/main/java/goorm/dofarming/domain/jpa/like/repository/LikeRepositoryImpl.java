package goorm.dofarming.domain.jpa.like.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static goorm.dofarming.domain.jpa.chatroom.entity.QChatroom.chatroom;
import static goorm.dofarming.domain.jpa.like.entity.QLike.*;
import static goorm.dofarming.domain.jpa.location.entity.QLocation.*;
import static goorm.dofarming.domain.jpa.user.entity.QUser.*;
import static org.springframework.util.StringUtils.hasText;

public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Like> search(Long userId, Long likeId, LocalDateTime updatedAt, Integer likeCount, Double avgScore, String title, List<String> themes, List<Region> regions, SortType sortType) {

        List<Long> likeIds = queryFactory
                .select(like.likeId)
                .from(like)
                .join(like.user, user)
                .join(like.location, location)
                .where(
                        userIdEq(userId),
                        cursorCondition(likeId, updatedAt, sortType),
                        cursorLike(likeCount, likeId, sortType),
                        cursorScore(avgScore, likeId, sortType),
                        titleContains(title),
                        themesEq(themes),
                        orRegion(regions),
                        statusEq(Status.ACTIVE)
                )
                .orderBy(
                        sortOrder(sortType),
                        like.likeId.desc()
                )
                .limit(20)
                .fetch();

        return queryFactory
                .select(like)
                .from(like)
                .join(like.location, location).fetchJoin()
                .where(
                        like.likeId.in(likeIds)
                )
                .orderBy(
                        sortOrder(sortType),
                        like.likeId.desc()
                )
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? like.user.userId.eq(userId) : null;
    }

    private BooleanExpression titleContains(String title) {
        return hasText(title) ? like.location.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression cursorCondition(Long likeId, LocalDateTime updatedAt, SortType sortType) {
        if (likeId == null || updatedAt == null) {
            return null;
        }

        if (sortType.equals(SortType.Earliest)) {
            return like.updatedAt.after(updatedAt)
                    .or(
                            like.updatedAt.eq(updatedAt)
                                    .and(like.likeId.gt(likeId))
                    );
        }

        return like.updatedAt.before(updatedAt)
                .or(
                        like.updatedAt.eq(updatedAt)
                                .and(like.likeId.lt(likeId))
                );
    }

    private BooleanExpression cursorLike(Integer likeCount, Long likeId, SortType sortType) {
        if (likeId == null || likeCount == null) {
            return null;
        }

        if (sortType.equals(SortType.LowLike)) {
            return like.location.likeCount.lt(likeCount)
                    .or(
                            like.location.likeCount.eq(likeCount)
                                    .and(like.likeId.lt(likeId))
                    );
        }

        return like.location.likeCount.gt(likeCount)
                .or(
                        like.location.likeCount.eq(likeCount)
                                .and(like.likeId.lt(likeId))
                );
    }

    private BooleanExpression cursorScore(Double avgScore, Long likeId, SortType sortType) {
        if (likeId == null || avgScore == null) {
            return null;
        }

        NumberExpression<Double> calcAvgScore = getAvgScoreExpression();

        if (sortType.equals(SortType.LowScore)) {
            return calcAvgScore.gt(avgScore)
                    .or(
                            calcAvgScore.eq(avgScore)
                                    .and(like.likeId.lt(likeId))
                    );
        }

        return calcAvgScore.lt(avgScore)
                .or(
                        calcAvgScore.eq(avgScore)
                                .and(like.likeId.lt(likeId))
                );
    }
    private BooleanExpression statusEq(Status status) {
        return hasText(status.name()) ? like.status.eq(status) : null;
    }

    private BooleanExpression themesEq(List<String> themes) {
        if (themes == null || themes.isEmpty()) {
            return null;
        }
        return like.location.theme.in(themes);
    }

    private BooleanBuilder orRegion(List<Region> regions) {
        if (regions == null || regions.isEmpty()) {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();
        for (Region region : regions) {
            builder.or(like.location.addr.containsIgnoreCase(region.name()));
        }
        return builder;
    }

    private OrderSpecifier sortOrder(SortType sortType) {
        switch (sortType) {
            case Earliest:
                return like.updatedAt.asc();
            case HighLike:
                return like.location.likeCount.desc();
            case LowLike:
                return like.location.likeCount.asc();
            case HighScore:
                NumberExpression<Double> HighAvgScore = getAvgScoreExpression();
                return HighAvgScore.desc();
            case LowScore:
                NumberExpression<Double> LowAvgScore = getAvgScoreExpression();
                return LowAvgScore.asc();
            default:
                return like.updatedAt.desc(); // 기본 정렬
        }
    }

    private static NumberExpression<Double> getAvgScoreExpression() {
        NumberExpression<Double> avgScore = Expressions.numberTemplate(Double.class, "coalesce(coalesce({0}, 0) / nullif({1}, 0), 0)", like.location.totalScore, like.location.reviewCount);
        return avgScore;
    }
}
