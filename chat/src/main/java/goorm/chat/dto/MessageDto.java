package goorm.chat.dto;

import goorm.chat.domain.MessageType;

import java.io.Serializable;

public record MessageDto(
        Long roomId,
        Long userId,
        String nickname,
        MessageType messageType,
        String content
) implements Serializable {
}
