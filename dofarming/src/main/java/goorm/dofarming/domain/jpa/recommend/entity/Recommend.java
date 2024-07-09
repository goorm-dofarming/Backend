package goorm.dofarming.domain.jpa.recommend.entity;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_id")
    private Long recommendId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "log_id")
    private Log log;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    //== 생성 메서드 ==//
    public static Recommend recommend(Log log, Location location) {

        recommendValidateDuplicate(log, location);

        Recommend recommend = new Recommend();
        recommend.addLog(log);
        recommend.addLocation(location);
        recommend.status = Status.ACTIVE;
        return recommend;
    }

    //== 연관관계 메서드 ==//
    public void addLog(Log log) {
        this.log = log;
        log.getRecommends().add(this);
    }

    public void addLocation(Location location) {
        this.location = location;
        location.getRecommends().add(this);
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
    }

    //== 중복 검증 메서드 ==//
    private static void recommendValidateDuplicate(Log log, Location location) {
        if (log.getRecommends().stream().anyMatch(recommend -> recommend.getLocation().equals(location))) {

        }
    }
}
