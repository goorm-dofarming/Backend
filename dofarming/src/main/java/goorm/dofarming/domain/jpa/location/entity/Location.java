package goorm.dofarming.domain.jpa.location.entity;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;
    // placeId + dataType 으로 데이터 조회 가능
    // 추천된 장소의 위도
//    private String latitude;
    // 추천된 장소의 경도
//    private String longitude;

    private Long placeId;

    private int dataType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Location location(Long placeId, Integer dataType) {
        Location location = new Location();
        location.dataType = dataType;
        location.placeId = placeId;
        location.status = Status.ACTIVE;
//        location.latitude = latitude;
//        location.longitude = longitude;
        return location;
    }

}
