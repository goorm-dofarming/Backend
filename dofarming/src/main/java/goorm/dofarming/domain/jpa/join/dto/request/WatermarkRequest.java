package goorm.dofarming.domain.jpa.join.dto.request;

public record WatermarkRequest(
        Long roomId,
        Long messageId
) {
}
