package goorm.dofarming.domain.jpa.chatroom.dto.request;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅방 생성 요청 정보를 담는 DTO")
public record ChatroomCreateRequest(
        @Schema(description = "채팅방 제목", example = "New Chatroom")
        String title,
        @Schema(description = "채팅방 지역", example = "SEOUL")
        Region region
) {
}
