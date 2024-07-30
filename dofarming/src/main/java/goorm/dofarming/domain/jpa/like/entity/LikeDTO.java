package goorm.dofarming.domain.jpa.like.entity;

public record LikeDTO(
        Long id,
        String type,
        String title,
        String addr,
        String tel,
        String image,
        Double mapX,
        Double mapY,
        int likeCount
) {
}
