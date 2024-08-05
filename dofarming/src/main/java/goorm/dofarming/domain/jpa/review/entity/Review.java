package goorm.dofarming.domain.jpa.review.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Setter
@Getter
@Entity
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    private Double score; // 1 ~ 5 점, 0.5점 단위
    private String content; // 리뷰 내용

//    private int likeCount = 0; // 리뷰 자체에 대한 좋아요??

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public void delete() {
        this.status = Status.DELETE;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setReview(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setReview(null);
    }

    public void addUser(User user) {
        this.user=user;
        user.getReviews().add(this);
    }

    public void addLocation(Location location) {
        this.location = location;
        location.getReviews().add(this);
    }

//    public void increaseLike() {
//        this.likeCount++;
//    }
//
//    public void decreaseLike() {
//        if (likeCount != 0) this.likeCount--;
//    }

    public Review() {
    }
}
