package goorm.dofarming.domain.jpa.chatroom.dto.response;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.tag.dto.response.TagResponse;
import goorm.dofarming.global.common.entity.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ChatroomResponse(
        Long roomId,
        String title,
        String regionName,
        String regionImageUrl,
        List<TagResponse> tags,
        Long participantCount,
        LocalDateTime createAt
) {
    public static ChatroomResponse of(Chatroom chatroom) {
        return new ChatroomResponse(
                chatroom.getRoomId(),
                chatroom.getTitle(),
                chatroom.getRegion().getName(),
                chatroom.getRegion().getImageUrl(),
                chatroom.getTags().stream().map(TagResponse::of).collect(Collectors.toList()),
                chatroom.getParticipantCount(),
                chatroom.getCreatedAt()
        );
    }
}
