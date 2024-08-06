package goorm.dofarming.domain.jpa.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 수정 요청 정보를 담는 DTO")
public record ReviewModifyRequest(
        @Schema(description = "점수", example = "4.5")
        Double score,

        @Schema(description = "리뷰 내용", example = "Good!")
        String content
) {
}
