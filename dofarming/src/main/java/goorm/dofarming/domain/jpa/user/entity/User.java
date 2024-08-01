package goorm.dofarming.domain.jpa.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.global.auth.GoogleOAuth2UserInfo;
import goorm.dofarming.global.auth.KakaoOAuth2UserInfo;
import goorm.dofarming.global.auth.NaverOAuth2UserInfo;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String socialId;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    //== 생성 메서드 ==//
    public static User user(String email, String nickname, String password) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.nickname = nickname;
        user.imageUrl = null;
        user.role = Role.DOFARMING;
        user.status = Status.ACTIVE;
        return user;
    }

    public static User of(String socialType, Map<String, Object> attribute) {
        if (socialType.equals("NAVER")) {
            return ofNaver(attribute);

        } else if (socialType.equals("KAKAO")) {
            return ofKakao(attribute);

        } else {
            return ofGoogle(attribute);
        }
    }

    private static User ofGoogle(Map<String, Object> attribute) {
        GoogleOAuth2UserInfo googleOAuth2UserInfo = new GoogleOAuth2UserInfo(attribute);
        User user = new User();
        user.socialId = googleOAuth2UserInfo.getId();
        user.email = googleOAuth2UserInfo.getEmail();
        user.nickname = googleOAuth2UserInfo.getNickname();
        user.imageUrl = googleOAuth2UserInfo.getImageUrl();
        user.role = Role.GOOGLE;
        user.status = Status.ACTIVE;
        return user;
    }

    private static User ofNaver(Map<String, Object> attribute) {
        NaverOAuth2UserInfo naverOAuth2UserInfo = new NaverOAuth2UserInfo(attribute);
        User user = new User();
        user.socialId = naverOAuth2UserInfo.getId();
        user.email = naverOAuth2UserInfo.getEmail();
        user.nickname = naverOAuth2UserInfo.getNickname();
        user.imageUrl = naverOAuth2UserInfo.getImageUrl();
        user.role = Role.NAVER;
        user.status = Status.ACTIVE;
        return user;
    }

    private static User ofKakao(Map<String, Object> attribute) {
        KakaoOAuth2UserInfo kakaoOAuth2UserInfo = new KakaoOAuth2UserInfo(attribute);
        User user = new User();
        user.socialId = kakaoOAuth2UserInfo.getId();
        user.email = kakaoOAuth2UserInfo.getEmail();
        user.nickname = kakaoOAuth2UserInfo.getNickname();
        user.imageUrl = kakaoOAuth2UserInfo.getImageUrl();
        user.role = Role.KAKAO;
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
