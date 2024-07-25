package goorm.dofarming.domain.jpa.like.entity;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.infra.tourapi.domain.Cafe;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Table(name = "likesV2")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeV2 extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "location_id")
//    private Location location;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    //== 생성 메서드 ==//
    public static LikeV2 like(User user, Cafe location) {

        LikeV2 likeEntity = likeValidateDuplicate(user, location);

        if (likeEntity == null) {
            LikeV2 like = new LikeV2();
            like.addUser(user);
            like.addCafe(location);
            like.status = Status.ACTIVE;
        } else {
            likeEntity.reverseStatus();
        }

        return likeEntity;
    }

    //== 연관관계 메서드 ==//
    public void addUser(User user) {
        this.user = user;
        user.getLikesV2().add(this);
    }

    public void addCafe(Cafe location) {
        this.cafe = location;
        cafe.getLikes().add(this);
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
    }

    public void active() {
        this.status = Status.ACTIVE;
    }

    public void reverseStatus() {
        if (this.status == Status.ACTIVE) {
            this.status = Status.DELETE;
        } else {
            this.status = Status.ACTIVE;
        }
    }

    //== 중복 검증 메서드 ==//
    private static LikeV2 likeValidateDuplicate(User user, Cafe location) {
        return user.getLikesV2().stream()
                .filter(like -> like.getCafe().equals(location))
                .findFirst()
                .orElse(null);
    }
}
