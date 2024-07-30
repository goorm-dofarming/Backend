package goorm.dofarming.domain.jpa.recommend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
