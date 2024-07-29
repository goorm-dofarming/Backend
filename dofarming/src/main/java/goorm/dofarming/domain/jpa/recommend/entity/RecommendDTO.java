package goorm.dofarming.domain.jpa.recommend.entity;

import lombok.Data;

import java.util.List;

@Data
public class RecommendDTO {

    private Long logId;
    private String address;
    private List<Object> recommendations;

    public RecommendDTO(Long logId, String address, List<Object> recommendations) {
        this.logId = logId;
        this.address = address;
        this.recommendations = recommendations;
    }
}
