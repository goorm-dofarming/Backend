package goorm.dofarming.domain.jpa.chatroom.dto.request;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import goorm.dofarming.domain.jpa.tag.dto.request.TagRequest;

import java.util.List;
import java.util.Set;

public record ChatroomCreateRequest(
        String title,
        Region region,
        List<TagRequest> tags
) {
}
