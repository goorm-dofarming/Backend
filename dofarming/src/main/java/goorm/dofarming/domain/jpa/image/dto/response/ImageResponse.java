package goorm.dofarming.domain.jpa.image.dto.response;

import goorm.dofarming.domain.jpa.image.entity.Image;

public record ImageResponse(
        Long imageId,
        String imageUrl
) {
    public static ImageResponse of(Image image) {
        return new ImageResponse(
                image.getImageId(),
                image.getImageUrl()
        );
    }
}
