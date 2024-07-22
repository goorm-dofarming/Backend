package goorm.dofarming.domain.jpa.chatroom.dto.request;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "채팅방 생성 요청 정보를 담는 DTO")
public record ChatroomCreateRequest(

        @Schema(description = "채팅방 제목", example = "Welcome to the new chatroom")
        String title,
        @Schema(description = "채팅방 지역", example = "서울특별시")
        Region region,
        @Schema(description = "채팅방 태그 목록", example = "[\"activity\", \"mountain\"]")
        List<String> tagNames
) {
}