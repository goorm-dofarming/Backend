package goorm.dofarming.infra.tourapi.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    private String title;
    private String addr;
    private String tel;
    private String image;
    private Double mapX;
    private Double mapY;

    @Column(name = "dataType")
    private int dataType = 3;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Transient
    private boolean isLiked;

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    public void incrementLikeCount() {
        System.out.println("activity increment");
        this.likeCount++;
    }

    public void decrementLikeCount() {
        System.out.println("activity decrement");
        this.likeCount--;
    }

    @JsonGetter("countLikes")
    public int getLikeCount() {
        return this.likeCount;
    }

    public Activity(String title, String addr, String tel, String image, Double mapX, Double mapY) {
        this.title = title;
        this.addr = addr;
        this.tel = tel;
        this.image = image;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public Activity() {
    }
}
