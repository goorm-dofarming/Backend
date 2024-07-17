package goorm.chat.dto;

import goorm.chat.domain.Message;
import goorm.chat.domain.MessageType;

import java.time.LocalDateTime;

public record MessageResponse(
        Long messageId,
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
