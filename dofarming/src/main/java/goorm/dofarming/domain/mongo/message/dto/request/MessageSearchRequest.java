package goorm.dofarming.domain.mongo.message.dto.request;

import java.time.LocalDateTime;

public record MessageSearchRequest(
        Long messageId,
        Long roomId,
        LocalDateTime createAt
) {
}
