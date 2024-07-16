package goorm.dofarming.domain.mongo.message.dto.response;

import goorm.dofarming.domain.mongo.message.entity.Message;
import goorm.dofarming.domain.mongo.message.entity.MessageType;

import java.time.LocalDateTime;

public record MessageResponse(
        String messageId,
        Long userId,
        String nickname,
        Long roomId,
        MessageType messageType,
        String content,
        LocalDateTime createAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getUserId(),
                message.getNickname(),
                message.getRoomId(),
                message.getMessageType(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
