package goorm.dofarming.domain.jpa.log.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Schema(description = "로그 엔티티")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends BaseEntity {

    @Schema(description = "로그 ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Schema(description = "테마", example = "ACTIVITY")
    private String theme;

    @Schema(description = "주소", example = "서울시 강남구")
    private String address;

    @Schema(description = "위도", example = "37.123456")
    private Double latitude;

    @Schema(description = "경도", example = "127.123456")
    private Double longitude;

    @Schema(description = "상태", example = "ACTIVE")
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
    public static Log log(Double longitude, Double latitude, String theme, String address, User user) {
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
        this.recommends.forEach(Recommend::delete);
    }
}
