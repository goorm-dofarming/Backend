package goorm.dofarming.domain.jpa.chatroom.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomSearchRequest;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import goorm.dofarming.global.common.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static goorm.dofarming.domain.jpa.chatroom.entity.QChatroom.*;
import static goorm.dofarming.domain.jpa.tag.entity.QTag.*;
import static org.springframework.util.StringUtils.*;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryImpl implements ChatroomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Chatroom> search(ChatroomSearchRequest request) {
        return queryFactory
                .select(chatroom).distinct()
                .from(chatroom)
                .leftJoin(chatroom.tags, tag).fetchJoin()
                .where(
                        titleEq(request.title()),
                        tagEq(request.tagName()),
                        regionEq(request.region()),
                        cursorCondition(request.roomId(), request.createdAt()),
                        statusEq(Status.ACTIVE)
                )
                .orderBy(
                        chatroom.createdAt.desc(),
                        chatroom.roomId.desc()
                )
                .limit(20)
                .fetch();
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? chatroom.title.containsIgnoreCase(title) : null;
    }
    private BooleanExpression regionEq(Region region) {
        return hasText(region.getName()) ? chatroom.region.eq(region) : null;
    }
    private BooleanExpression tagEq(String tagName) {
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
