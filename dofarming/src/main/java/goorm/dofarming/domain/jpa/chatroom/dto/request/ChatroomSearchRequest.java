package goorm.dofarming.domain.jpa.chatroom.dto.request;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;

import java.time.LocalDateTime;

public record ChatroomSearchRequest(
        Long roomId,
        String title,
        String tagName,
        Region region,
        LocalDateTime createdAt
) {
}
