package goorm.dofarming.domain.jpa.join.dto.request;

import goorm.dofarming.domain.mongo.message.dto.MessageDto;

public record AlarmDto(
        Long receiveId,
        String content
) {
    public static AlarmDto of(Long receiveId, MessageDto messageDto) {
        return new AlarmDto (
                receiveId,
                messageDto.content()
        );
    }
}
