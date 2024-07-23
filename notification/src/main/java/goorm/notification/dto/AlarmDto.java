package goorm.notification.dto;
public record AlarmDto(
        Long receiveId,
        Long roomId,
        String content
) {
}
