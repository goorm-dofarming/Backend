package goorm.dofarming.domain.jpa.chatroom.dto.response;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.global.common.entity.Status;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ChatroomResponse(
        Long roomId,
        String title,
        String regionName,
        String regionImageUrl,
        Long participantCount,
        LocalDateTime createAt
) {
    public static ChatroomResponse of(Chatroom chatroom) {
        return new ChatroomResponse(
                chatroom.getRoomId(),
                chatroom.getTitle(),
                chatroom.getRegion().getName(),
                chatroom.getRegion().getImageUrl(),
                chatroom.getParticipantCount(),
                chatroom.getCreatedAt()
        );
    }
}
