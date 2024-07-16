package goorm.dofarming.domain.mongo.message.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import goorm.dofarming.domain.mongo.message.dto.request.MessageSearchRequest;
import goorm.dofarming.domain.mongo.message.entity.Message;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;

import static goorm.dofarming.domain.mongo.message.entity.QMessage.*;

public class MessageRepositoryImpl extends QuerydslRepositorySupport implements MessageRepositoryCustom {

    public MessageRepositoryImpl(MongoOperations operations) {
        super(operations);
    }

    @Override
    public List<Message> search(MessageSearchRequest request) {
        return from(message)
                .where(
                        roomIdEq(request.roomId()),
                        cursorCondition(request.messageId(), request.createAt())
                )
                .orderBy(
                        message.createdAt.desc(),
                        message.id.desc()
                )
                .limit(50)
                .fetch();
    }

    private BooleanExpression roomIdEq(Long roomId) {
        return roomId != null ? message.roomId.eq(roomId) : null;
    }
    private BooleanExpression cursorCondition(String id, LocalDateTime createdAt) {
        if (id == null || createdAt == null) {
            return null;
        }
        return message.createdAt.before(createdAt)
                .or(message.createdAt.eq(createdAt).and(message.id.lt(id)));
    }
}
