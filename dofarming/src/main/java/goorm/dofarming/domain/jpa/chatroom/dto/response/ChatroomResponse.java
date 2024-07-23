package goorm.dofarming.domain.jpa.chatroom.dto.response;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.tag.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "채팅방 응답 정보를 담는 DTO")
public record ChatroomResponse(

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

        @Schema(description = "채팅방 생성 시간", example = "2023-07-20T12:55:56")
        LocalDateTime createdAt
) {
    public static ChatroomResponse of(Chatroom chatroom) {
        return new ChatroomResponse(
                chatroom.getRoomId(),
                chatroom.getTitle(),
                chatroom.getRegion().name(),
                chatroom.getTags().stream().map(TagResponse::of).collect(Collectors.toList()),
                chatroom.getParticipantCount(),
                chatroom.getCreatedAt()
        );
    }
}
