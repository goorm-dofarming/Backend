package goorm.dofarming.domain.jpa.review.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReviewCreateRequest(
        Long locationId,
        Double score,
        String content
) {
}
