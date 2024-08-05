package goorm.dofarming.domain.jpa.review_like.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long reviewLikeId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;


    //== 생성 메서드 ==//
    public static ReviewLike reviewLike(User user, Review review) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.addUser(user);
        reviewLike.addReview(review);
        reviewLike.status = Status.ACTIVE;
        review.increaseReviewLike();
        return reviewLike;
    }

    //== 연관관계 메서드 ==//
    public void addUser(User user) {
        this.user = user;
        user.getReviewLikes().add(this);
    }

    public void addReview(Review review) {
        this.review = review;
        review.getReviewLikes().add(this);
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
        this.getReview().decreaseReviewLike();
    }

    public void active() {
        this.status = Status.ACTIVE;
        this.getReview().increaseReviewLike();
    }
}
