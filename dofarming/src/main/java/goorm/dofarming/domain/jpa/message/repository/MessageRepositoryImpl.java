package goorm.dofarming.domain.jpa.message.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.message.entity.Message;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<Message> search(LocalDateTime time, Long lastReadMessageId, Long messageId, Long roomId, LocalDateTime createdAt) {
        List<Message> unreadMessage = queryFactory
                .select(message)
                .from(message)
                .join(message.join, join).fetchJoin()
                .join(join.chatroom, chatroom).fetchJoin()
                .where(
                        roomIdEq(roomId),
                        cursorCondition(messageId, createdAt),
                        afterChat(lastReadMessageId),
                        entryTime(time)
                )
                .orderBy(
                        message.createdAt.desc(),
                        message.messageId.desc()
                )
                .fetch();

        List<Message> paging = queryFactory
                .select(message)
                .from(message)
                .where(
                        roomIdEq(roomId),
                        cursorCondition(messageId, createdAt),
                        entryTime(time)
                )
                .orderBy(
                        message.createdAt.desc(),
                        message.messageId.desc()
                )
                .limit(50)
                .fetch();


        List<Message> previousMessage = queryFactory
                .select(message)
                .from(message)
                .join(message.join, join).fetchJoin()
                .join(join.chatroom, chatroom).fetchJoin()
                .where(
                        message.in(paging),
                        beforeChat(lastReadMessageId)
                )
                .orderBy(
                        message.createdAt.desc(),
                        message.messageId.desc()
                )
                .fetch();

        List<Message> result = new ArrayList<>();
        result.addAll(unreadMessage);
        result.addAll(previousMessage);

        return result;

    }
    private BooleanExpression beforeChat(Long lastReadMessageId) {
        return message.messageId.loe(lastReadMessageId);
    }

    private BooleanExpression afterChat(Long lastReadMessageId) {
        return message.messageId.gt(lastReadMessageId);
    }
    private BooleanExpression entryTime(LocalDateTime time) {
        return time != null ? message.createdAt.after(time) : null;
    }
    private BooleanExpression roomIdEq(Long roomId) {
        return roomId != null ? message.chatroom.roomId.eq(roomId) : null;
    }
    private BooleanExpression cursorCondition(Long messageId, LocalDateTime createdAt) {
        if (messageId == null || createdAt == null) {
            return null;
        }
        return message.createdAt.before(createdAt)
                .or(message.createdAt.eq(createdAt).and(message.messageId.lt(messageId)));
    }
}
