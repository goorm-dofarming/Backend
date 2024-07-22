package goorm.dofarming.infra.tourapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Tour API 응답 항목 정보를 담는 DTO")
public record Item(

        @Schema(description = "타이틀", example = "TourTitle")
        @JsonProperty("title")
        String title,

        @Schema(description = "주소", example = "Seoul, South Korea")
        @JsonProperty("addr1")
        String addr,

        @Schema(description = "전화번호", example = "+82-2-1234-5678")
        @JsonProperty("tel")
        String tel,

        @Schema(description = "지도 X 좌표", example = "126.734086")
        @JsonProperty("mapx")
        String mapX,

        @Schema(description = "지도 Y 좌표", example = "37.413294")
        @JsonProperty("mapy")
        String mapY,

        @Schema(description = "첫 번째 이미지 URL", example = "http://example.com/image.jpg")
        @JsonProperty("firstimage")
        String image,

        @Schema(description = "콘텐츠 ID", example = "123456")
        @JsonProperty("contentid")
        String contentId,

        @Schema(description = "카테고리 3", example = "A03")
        @JsonProperty("cat3")
        String cat3,

        @Schema(description = "카테고리 1", example = "A01")
        @JsonProperty("cat1")
        String cat1,

        @Schema(description = "카테고리 2", example = "A02")
        @JsonProperty("cat2")
        String cat2

) {
}
