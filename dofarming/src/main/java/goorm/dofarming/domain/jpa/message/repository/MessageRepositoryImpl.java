package goorm.dofarming.domain.jpa.message.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.message.entity.Message;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static goorm.dofarming.domain.jpa.chatroom.entity.QChatroom.*;
import static goorm.dofarming.domain.jpa.join.entity.QJoin.*;
import static goorm.dofarming.domain.jpa.message.entity.QMessage.*;


public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MessageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<Message> search(Long messageId, Long roomId, LocalDateTime createdAt) {
        return queryFactory
                .select(message)
                .from(message)
                .join(message.join, join).fetchJoin()
                .join(join.chatroom, chatroom).fetchJoin()
                .where(
                        roomIdEq(roomId),
                        cursorCondition(messageId, createdAt)
                )
                .orderBy(
                        message.createdAt.desc(),
                        message.messageId.desc()
                )
                .limit(50)
                .fetch();
    }

    private BooleanExpression roomIdEq(Long roomId) {
        return roomId != null ? join.chatroom.roomId.eq(roomId) : null;
    }
    private BooleanExpression cursorCondition(Long messageId, LocalDateTime createdAt) {
        if (messageId == null || createdAt == null) {
            return null;
        }
        return message.createdAt.before(createdAt)
                .or(message.createdAt.eq(createdAt).and(message.messageId.lt(messageId)));
    }
}
