package goorm.dofarming.domain.jpa.like.entity;

public record LikeDTO(
        Long like_id,
        int dataType,
        Long title_id,
        String title,
        String addr,
        String tel,
        String image,
        Double mapX,
        Double mapY,
        int likeCount,
        boolean isLiked
) {
}
