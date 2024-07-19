package goorm.dofarming.domain.mongo.message.dto;

import goorm.dofarming.domain.mongo.message.entity.MessageType;

import java.io.Serializable;

public record MessageDto(
        Long roomId,
        Long userId,
        String nickname,
        MessageType messageType,
        String content
) implements Serializable {

}
