package goorm.dofarming.domain.jpa.log.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    private String theme;

    // 일단 좌표만 보내는 걸로...
//    private String address;
    // 추천된 위치의 위도
    private Double latitude;
    // 추천된 위치의 경도
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Recommend> recommends = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Log log(Double longitude, Double latitude, String theme, User user) {
        Log log = new Log();
        log.theme = theme;
//        log.address = address;
        log.latitude = latitude;
        log.longitude = longitude;
        log.status = Status.ACTIVE;
        log.addUser(user);
        return log;
    }

    //== 연관관계 메서드 ==//
    public void addUser(User user) {
        this.user = user;
        user.getLogs().add(this);
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
        this.recommends.stream().forEach(Recommend::delete);
    }
}
