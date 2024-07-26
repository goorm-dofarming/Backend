package goorm.dofarming.domain.jpa.chatroom.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.join.entity.QJoin;
import goorm.dofarming.domain.jpa.user.entity.QUser;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.EntityManager;

import static goorm.dofarming.domain.jpa.chatroom.entity.QChatroom.*;
import static goorm.dofarming.domain.jpa.join.entity.QJoin.*;
import static goorm.dofarming.domain.jpa.tag.entity.QTag.*;
import static goorm.dofarming.domain.jpa.user.entity.QUser.*;
import static org.springframework.util.StringUtils.*;

import java.time.LocalDateTime;
import java.util.List;

public class ChatroomRepositoryImpl implements ChatroomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatroomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<Chatroom> search(Long roomId, String condition, LocalDateTime createdAt) {

        BooleanBuilder orBuilder = new BooleanBuilder()
                .or(titleContains(condition))
                .or(regionContains(condition))
                .or(tagContains(condition));

        return queryFactory
                .selectDistinct(chatroom)
                .from(chatroom)
                .leftJoin(chatroom.tags, tag).fetchJoin()
                .where(
                        orBuilder,
                        cursorCondition(roomId, createdAt),
                        statusEq(Status.ACTIVE)
                )
                .orderBy(
                        chatroom.createdAt.desc(),
                        chatroom.roomId.desc()
                )
                .limit(20)
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
    private BooleanExpression cursorCondition(Long roomId, LocalDateTime createdAt) {
        if (roomId == null || createdAt == null) {
            return null;
        }
        return chatroom.createdAt.before(createdAt)
                .or(chatroom.createdAt.eq(createdAt).and(chatroom.roomId.lt(roomId)));
    }
    private BooleanExpression statusEq(Status status) {
        return hasText(status.name()) ? chatroom.status.eq(status) : null;
    }
}
