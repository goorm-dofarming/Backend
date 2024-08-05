package goorm.dofarming.domain.jpa.review.dto.request;

public record ReviewModifyRequest(
        Double score,
        String content
) {
}
