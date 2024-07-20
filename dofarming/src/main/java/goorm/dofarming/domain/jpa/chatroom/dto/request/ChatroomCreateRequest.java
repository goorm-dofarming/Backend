package goorm.dofarming.domain.jpa.chatroom.dto.request;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;

import java.util.List;

public record ChatroomCreateRequest(
        String title,
        Region region,
        List<String> tagNames
) {
}