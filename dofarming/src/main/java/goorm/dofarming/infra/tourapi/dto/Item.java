package goorm.dofarming.infra.tourapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Item(
        @JsonProperty("title") String title,
        @JsonProperty("addr1") String addr,
        @JsonProperty("tel") String tel,
        @JsonProperty("mapx") String mapX,
        @JsonProperty("mapy") String mapY,
        @JsonProperty("firstimage") String image,
        @JsonProperty("contentid") String contentId,
        @JsonProperty("cat3") String cat3,
        @JsonProperty("cat1") String cat1,
        @JsonProperty("cat2") String cat2
) {
}
