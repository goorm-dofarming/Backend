package goorm.dofarming.domain.jpa.join.dto.request;

import goorm.dofarming.domain.jpa.message.dto.MessageDto;
import goorm.dofarming.domain.jpa.message.entity.MessageType;

public record AlarmDto(
        Long receiveId,
        Long senderId,
        Long roomId,
        MessageType messageType,
        String content
) {
    public static AlarmDto of(Long receiveId, MessageDto messageDto) {
        return new AlarmDto (
                receiveId,
                messageDto.userId(),
                messageDto.roomId(),
                messageDto.messageType(),
                messageDto.content()
        );
    }
}
