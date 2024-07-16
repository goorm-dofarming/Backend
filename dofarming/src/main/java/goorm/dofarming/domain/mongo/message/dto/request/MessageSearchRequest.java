package goorm.dofarming.domain.mongo.message.dto.request;

import java.time.LocalDateTime;

public record MessageSearchRequest(
        String messageId,
        Long roomId,
        LocalDateTime createAt
) {
}
