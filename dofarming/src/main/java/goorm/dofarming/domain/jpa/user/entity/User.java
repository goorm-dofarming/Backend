package goorm.dofarming.domain.jpa.user.entity;

import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.log.entity.Log;
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
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String email;

    private String password;

    private String imageUrl;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Log> logs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Join> joins = new ArrayList<>();

    //== 생성 메서드 ==//
    public static User user(String email, String nickname, String password) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.nickname = nickname;
        user.imageUrl = null;
        user.role = Role.GUEST;
        user.status = Status.ACTIVE;
        return user;
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
        this.getLogs().stream().forEach(Log::delete);
        this.getJoins().stream().forEach(Join::delete);
    }

    public void encoder(String password) {
        this.password = password;
    }

    public void updateInfo(String nickname, String password, String imageUrl) {
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
    }
}
