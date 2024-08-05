package goorm.dofarming.domain.jpa.location.entity;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.infra.tourapi.domain.Activity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "theme")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;
    private String title;
    private String addr;
    private String tel;
    private String image;
    private Double mapX;
    private Double mapY;
    @Column(insertable = false, updatable = false)
    private String theme;
    private int likeCount = 0;
    private int reviewCount = 0;
    private Double totalScore = 0.0;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Location (String title, String addr, String tel, String image, double mapx, double mapy) {
        this.title = title;
        this.image = image;
        this.addr = addr;
        this.tel = tel;
        this.mapX = mapx;
        this.mapY = mapy;
        this.status = Status.ACTIVE;
    }

    public void increaseLike() {
        this.likeCount++;
    }

    public void decreaseLike() {
        if (likeCount != 0) this.likeCount--;
    }

    public void increaseReview() {
        this.reviewCount++;
    }

    public void decreaseReview() {
        if (reviewCount != 0) this.reviewCount--;
    }

    public void addScore(Double score) {
        this.totalScore += score;
    }

    public void subScore(Double score) {
        this.totalScore -= score;
    }

    public String avgScore() {
        return String.format("%.1f", this.totalScore / reviewCount);
    }
}
