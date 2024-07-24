package goorm.dofarming.domain.jpa.chatroom.dto.response;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.tag.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record MyChatroomResponse(

        @Schema(description = "조인 ID", example = "1")
        Long joinId,

        @Schema(description = "채팅방 ID", example = "1")
        Long roomId,

        @Schema(description = "채팅방 제목", example = "Welcome!")
        String title,

        @Schema(description = "채팅방 지역명", example = "서울특별시")
        String regionName,

        @Schema(description = "채팅방 태그 목록")
        List<TagResponse> tags,

        @Schema(description = "참가자 수", example = "5")
        Long participantCount,

        @Schema(description = "안 읽은 메시지 수", example = "34")
        Long unreadMessageCount,

        @Schema(description = "채팅방 생성 시간", example = "2023-07-20T12:55:56")
        LocalDateTime createdAt
) {
    public static MyChatroomResponse of(Join join) {
        return new MyChatroomResponse(
                join.getJoinId(),
                join.getChatroom().getRoomId(),
                join.getChatroom().getTitle(),
                join.getChatroom().getRegion().name(),
                join.getChatroom().getTags().stream().map(TagResponse::of).collect(Collectors.toList()),
                join.getChatroom().getParticipantCount(),
                join.getChatroom().getMessages().stream().filter(message -> message.getMessageId() > join.getLastReadMessage().getMessageId()).count(),
                join.getChatroom().getCreatedAt()
        );
    }
}
