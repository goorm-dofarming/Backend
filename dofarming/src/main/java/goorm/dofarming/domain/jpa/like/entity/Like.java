package goorm.dofarming.domain.jpa.like.entity;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.user.entity.User;
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
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    //== 생성 메서드 ==//
    public static Like like(User user, Location location) {

        likeValidateDuplicate(user, location);

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
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
    }

    public void active() {
        this.status = Status.ACTIVE;
    }

    //== 중복 검증 메서드 ==//
    private static void likeValidateDuplicate(User user, Location location) {
        if (user.getLikes().stream().anyMatch(like -> like.getLocation().equals(location))) {

        }
    }
}
