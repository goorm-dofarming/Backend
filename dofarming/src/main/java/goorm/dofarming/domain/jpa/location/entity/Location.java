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
    // 위도
    private String latitude;
    // 경도
    private String longitude;

    private String placeId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Location location(String latitude, String longitude, String placeId) {
        Location location = new Location();
        location.latitude = latitude;
        location.longitude = longitude;
        location.status = Status.ACTIVE;
        return location;
    }
}
