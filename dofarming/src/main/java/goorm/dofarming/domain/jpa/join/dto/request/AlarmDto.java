package goorm.dofarming.domain.jpa.join.dto.request;

import goorm.dofarming.domain.jpa.message.dto.MessageDto;

public record AlarmDto(
        Long receiveId,
        Long roomId,
        String content
) {
    public static AlarmDto of(Long receiveId, MessageDto messageDto) {
        return new AlarmDto (
                receiveId,
                messageDto.roomId(),
                messageDto.content()
        );
    }
}
