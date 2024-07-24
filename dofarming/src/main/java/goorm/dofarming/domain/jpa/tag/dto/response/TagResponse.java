package goorm.dofarming.domain.jpa.tag.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import goorm.dofarming.domain.jpa.tag.entity.Tag;

@Schema(description = "태그 응답 정보를 담는 DTO")
public record TagResponse(

        @Schema(description = "태그 이름", example = "Activity")
        String name,

        @Schema(description = "태그 색상", example = "#ED4A51")
        String color
) {

    public static TagResponse of(Tag tag) {
        return new TagResponse(
                tag.getName(),
                tag.getColor()
        );
    }
}
