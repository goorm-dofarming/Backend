package goorm.dofarming.domain.jpa.image.dto.response;

import goorm.dofarming.domain.jpa.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 응답 정보를 담는 DTO")
public record ImageResponse(
        Long imageId,

        @Schema(description = "이미지 URL", example = "http://example.com/image.jpg")
        String imageUrl
) {
    public static ImageResponse of(Image image) {
        return new ImageResponse(
                image.getImageId(),
                image.getImageUrl()
        );
    }
}
