package goorm.dofarming.domain.jpa.log.entity;

import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    private String theme;

    private String address;
    // 위도
    private String latitude;
    // 경도
    private String longitude;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Log log(String theme, String address, String latitude, String longitude, User user) {
        Log log = new Log();
        log.theme = theme;
        log.address = address;
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
