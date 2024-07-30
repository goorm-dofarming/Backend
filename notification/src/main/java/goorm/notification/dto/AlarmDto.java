package goorm.notification.dto;
public record AlarmDto(
        Long receiveId,
        Long senderId,
        Long roomId,
        MessageType messageType,
        String content
) {
}
