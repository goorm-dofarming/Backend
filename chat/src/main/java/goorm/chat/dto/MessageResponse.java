package goorm.chat.dto;

import goorm.chat.domain.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MessageResponse(
        @Schema(description = "메시지 ID", example = "1sdfxc9lskji1")
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
}
