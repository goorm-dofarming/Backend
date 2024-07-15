package goorm.dofarming.domain.jpa.chatroom.dto.request;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;

public record ChatroomCreateRequest(
        String title,
        Region region
) {
}
