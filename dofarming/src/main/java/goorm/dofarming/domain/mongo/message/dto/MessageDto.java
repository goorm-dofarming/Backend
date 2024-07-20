package goorm.dofarming.domain.mongo.message.dto;

import goorm.dofarming.domain.mongo.message.entity.MessageType;

public record MessageDto(
        Long roomId,
        Long userId,
        String nickname,
        MessageType messageType,
        String content
) implements Seri {

}