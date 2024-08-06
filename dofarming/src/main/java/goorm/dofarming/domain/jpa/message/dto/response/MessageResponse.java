package goorm.dofarming.domain.jpa.message.dto.response;

import goorm.dofarming.domain.jpa.message.entity.Message;
import goorm.dofarming.domain.jpa.message.entity.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "메시지 응답 정보를 담는 DTO")
public record MessageResponse(
        @Schema(description = "메시지 ID", example = "1")
        Long messageId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "사용자 닉네임", example = "usernickname")
        String nickname,

        @Schema(description = "채팅방 ID", example = "1")
        Long roomId,

        @Schema(description = "메시지 타입", example = "SEND")
        MessageType messageType,

        @Schema(description = "메시지 내용", example = "Hello!")
        String content,

        @Schema(description = "메시지 생성 시간", example = "2024-07-20T12:34:56")
        LocalDateTime createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getMessageId(),
                message.getJoin().getUser().getUserId(),
                message.getNickname(),
                message.getJoin().getChatroom().getRoomId(),
                message.getMessageType(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
