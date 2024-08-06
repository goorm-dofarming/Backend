package goorm.dofarming.domain.jpa.join.dto.request;

import goorm.dofarming.domain.jpa.message.dto.MessageDto;
import goorm.dofarming.domain.jpa.message.entity.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 정보를 담는 DTO")
public record AlarmDto(
        @Schema(description = "수신자 ID", example = "1")
        Long receiveId,

        @Schema(description = "발신자 ID", example = "2")
        Long senderId,

        @Schema(description = "채팅방 ID", example = "1")
        Long roomId,

        @Schema(description = "메시지 타입", example = "text")
        MessageType messageType,

        @Schema(description = "메시지 내용", example = "Hello!")
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
