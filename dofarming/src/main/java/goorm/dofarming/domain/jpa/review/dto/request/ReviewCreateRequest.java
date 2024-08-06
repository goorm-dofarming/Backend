package goorm.dofarming.domain.jpa.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 생성 요청 정보를 담는 DTO")
public record ReviewCreateRequest(
        @Schema(description = "장소 ID", example = "1")
        Long locationId,

        @Schema(description = "점수", example = "4.5")
        Double score,

        @Schema(description = "리뷰 내용", example = "Great!")
        String content
) {
}
