package goorm.dofarming.domain.jpa.join.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.message.entity.QMessage;
import goorm.dofarming.domain.jpa.user.entity.QUser;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static goorm.dofarming.domain.jpa.chatroom.entity.QChatroom.chatroom;
import static goorm.dofarming.domain.jpa.join.entity.QJoin.join;
import static goorm.dofarming.domain.jpa.message.entity.QMessage.*;
import static goorm.dofarming.domain.jpa.tag.entity.QTag.tag;
import static goorm.dofarming.domain.jpa.user.entity.QUser.*;
import static org.springframework.util.StringUtils.hasText;

public class JoinRepositoryImpl implements JoinRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public JoinRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Join> search(Long userId, String condition) {
        BooleanBuilder orBuilder = new BooleanBuilder()
                .or(titleContains(condition))
                .or(regionContains(condition))
                .or(tagContains(condition));

        return queryFactory
                .select(join)
                .from(join)
                .join(join.user, user).fetchJoin()
                .join(join.chatroom, chatroom).fetchJoin()
                .leftJoin(chatroom.messages, message)
                .leftJoin(chatroom.tags, tag)
                .where(
                        userIdEq(userId),
                        orBuilder,
                        joinStatusEq(Status.ACTIVE),
                        roomStatusEq(Status.ACTIVE)
                )
                .orderBy(
                        message.createdAt.desc(),
                        message.messageId.desc()
                )
                .fetch();
    }

    private BooleanExpression titleContains(String title) {
        return hasText(title) ? chatroom.title.containsIgnoreCase(title) : null;
    }
    private BooleanExpression regionContains(String region) {
        return hasText(region) ? chatroom.region.stringValue().containsIgnoreCase(region) : null;
    }
    private BooleanExpression tagContains(String tagName) {
        return hasText(tagName) ? tag.name.containsIgnoreCase(tagName) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.userId.eq(userId) : null;
    }

    private BooleanExpression joinStatusEq(Status status) {
        return hasText(status.name()) ? join.status.eq(status) : null;
    }

    private BooleanExpression roomStatusEq(Status status) {
        return hasText(status.name()) ? chatroom.status.eq(status) : null;
    }
}
