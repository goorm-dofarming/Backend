package goorm.dofarming.domain.jpa.join.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "읽은 메시지 갱신 요청 정보를 담는 DTO")
public record WatermarkRequest(

        @Schema(description = "채팅방 ID", example = "1")
        Long roomId,

        @Schema(description = "메시지 ID", example = "100")
        Long messageId
) {
}
