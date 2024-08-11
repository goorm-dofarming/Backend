package goorm.dofarming.domain.jpa.review.entity;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewCreateRequest;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewModifyRequest;
import goorm.dofarming.domain.jpa.review_like.entity.ReviewLike;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    private Double score; // 1 ~ 5 점, 0.5점 단위
    private String content; // 리뷰 내용

    private int reviewLikeCount = 0; // 리뷰 자체에 대한 좋아요??

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static Review review(User user, Location location, ReviewCreateRequest request) {
        Review review = new Review();
        review.content = request.content();
        review.score = request.score();
        review.status = Status.ACTIVE;
        review.addUser(user);
        review.addLocation(location);
        location.increaseReview();
        location.addScore(request.score());
        return review;
    }

    public void addUser(User user) {
        this.user = user;
        user.getReviews().add(this);
    }

    public void addLocation(Location location) {
        this.location = location;
        location.getReviews().add(this);
    }

    public void update(ReviewModifyRequest request) {
        this.content = request.content();

        this.getLocation().subScore(this.score);
        this.score = request.score();
        this.getLocation().addScore(request.score());
    }
    public void delete() {
        this.status = Status.DELETE;
        this.getLocation().decreaseReview();
        this.getLocation().subScore(this.score);

        for (Image image : images) {
            image.delete();
        }
    }

    public void increaseReviewLike() {
        this.reviewLikeCount++;
    }

    public void decreaseReviewLike() {
        if (reviewLikeCount != 0) this.reviewLikeCount--;
    }
}
