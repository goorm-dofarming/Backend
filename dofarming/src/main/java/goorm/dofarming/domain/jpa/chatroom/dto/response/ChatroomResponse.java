package goorm.dofarming.domain.jpa.chatroom.dto.response;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;

import java.time.LocalDateTime;

public record ChatroomResponse(
        Long roomId,
        String title,
        String regionName,
        String regionImageUrl,
        Integer participantCount,
        LocalDateTime createAt
) {
    public static ChatroomResponse of(Chatroom chatroom) {
        return new ChatroomResponse(
                chatroom.getRoomId(),
                chatroom.getTitle(),
                chatroom.getRegion().getName(),
                chatroom.getRegion().getImageUrl(),
                chatroom.getJoins().size(),
                chatroom.getCreatedAt()
        );
    }
}
