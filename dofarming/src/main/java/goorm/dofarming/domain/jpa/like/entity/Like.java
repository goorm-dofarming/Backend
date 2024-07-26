package goorm.dofarming.domain.jpa.like.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.infra.tourapi.domain.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Table(name = "likes")
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
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ocean_id")
    @JsonIgnore
    private Ocean ocean;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mountain_id")
    @JsonIgnore
    private Mountain mountain;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activity_id")
    @JsonIgnore
    private Activity activity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tour_id")
    @JsonIgnore
    private Tour tour;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id")
    @JsonIgnore
    private Cafe cafe;

    //== 생성 메서드 ==//
    public static <T> Like like(User user, T location) {

        Like likeEntity = likeValidateDuplicate(user, location);

        if (likeEntity == null) {
            Like like = new Like();
            like.addUser(user);
            like.status = Status.ACTIVE;
            like.addLocation(location);
            return like;
        } else {
            likeEntity.reverseStatus();
            return likeEntity;
        }

    }

    //== 연관관계 메서드 ==//
    public void addUser(User user) {
        this.user = user;
        user.getLikes().add(this);
    }

    public void addLocation(Object location) {
        if (location instanceof Cafe) {
            this.cafe = (Cafe) location;
            ((Cafe) location).getLikes().add(this);
            cafe.incrementLikeCount();
        } else if (location instanceof Ocean) {
            this.ocean = (Ocean) location;
            ((Ocean) location).getLikes().add(this);
            ocean.incrementLikeCount();
        } else if (location instanceof Mountain) {
            this.mountain = (Mountain) location;
            ((Mountain) location).getLikes().add(this);
            mountain.incrementLikeCount();
        } else if (location instanceof Activity) {
            this.activity = (Activity) location;
            ((Activity) location).getLikes().add(this);
            activity.incrementLikeCount();
        } else if (location instanceof Tour) {
            this.tour = (Tour) location;
            ((Tour) location).getLikes().add(this);
            tour.incrementLikeCount();
        } else if (location instanceof Restaurant) {
            this.restaurant = (Restaurant) location;
            ((Restaurant) location).getLikes().add(this);
            restaurant.incrementLikeCount();
        }
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        if (this.status == Status.ACTIVE) {
            decrementLikeCount();
        }
        this.status = Status.DELETE;
    }

    public void active() {
        System.out.println("ACTIVE????");
        if (this.status != Status.ACTIVE) {
            System.out.println("ACTIVE!!!!");
            incrementLikeCount();
        }
        this.status = Status.ACTIVE;
    }

    public void reverseStatus() {
        System.out.println("REVERSE!!!!");
        if (this.status == Status.ACTIVE) {
            this.status = Status.DELETE;
            System.out.println("MINUS!!!!");
            decrementLikeCount();
        } else {
            this.status = Status.ACTIVE;
            System.out.println("PLUS!!!!");
            incrementLikeCount();
        }
    }

    private static Like likeValidateDuplicate(User user, Object location) {
        return user.getLikes().stream()
                .filter(like -> {
                    if (location instanceof Ocean) {
                        return like.getOcean() != null && like.getOcean().equals(location);
                    } else if (location instanceof Mountain) {
                        return like.getMountain() != null && like.getMountain().equals(location);
                    } else if (location instanceof Activity) {
                        return like.getActivity() != null && like.getActivity().equals(location);
                    } else if (location instanceof Tour) {
                        return like.getTour() != null && like.getTour().equals(location);
                    } else if (location instanceof Restaurant) {
                        return like.getRestaurant() != null && like.getRestaurant().equals(location);
                    } else if (location instanceof Cafe) {
                        return like.getCafe() != null && like.getCafe().equals(location);
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    private void incrementLikeCount() {
        if (this.cafe != null) {
            this.cafe.incrementLikeCount();
        } else if (this.ocean != null) {
            this.ocean.incrementLikeCount();
        } else if (this.mountain != null) {
            this.mountain.incrementLikeCount();
        } else if (this.activity != null) {
            this.activity.incrementLikeCount();
        } else if (this.tour != null) {
            this.tour.incrementLikeCount();
        } else if (this.restaurant != null) {
            this.restaurant.incrementLikeCount();
        } else {
            System.out.println("ERROR");
        }
    }

    private void decrementLikeCount() {
        if (this.cafe != null) {
            this.cafe.decrementLikeCount();
        } else if (this.ocean != null) {
            this.ocean.decrementLikeCount();
        } else if (this.mountain != null) {
            this.mountain.decrementLikeCount();
        } else if (this.activity != null) {
            this.activity.decrementLikeCount();
        } else if (this.tour != null) {
            this.tour.decrementLikeCount();
        } else if (this.restaurant != null) {
            this.restaurant.decrementLikeCount();
        }
    }

}
