package goorm.dofarming.domain.jpa.like.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "좋아요 정보를 담는 엔티티")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    @Schema(description = "좋아요 ID", example = "1")
    private Long likeId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "상태", example = "ACTIVE")
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "사용자 정보", implementation = User.class)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    @Schema(description = "장소 정보", implementation = Location.class)
    private Location location;

    //== 생성 메서드 ==//
    public static Like like(User user, Location location) {
        Like like = new Like();
        like.addUser(user);
        like.addLocation(location);
        like.status = Status.ACTIVE;
        return like;

    }

    //== 연관관계 메서드 ==//
    public void addUser(User user) {
        this.user = user;
        user.getLikes().add(this);
    }

    public void addLocation(Location location) {
        this.location = location;
        location.getLikes().add(this);
        this.getLocation().increaseLike();
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
        this.getLocation().decreaseLike();
    }

    public void active() {
        this.status = Status.ACTIVE;
        this.getLocation().increaseLike();
    }
}
