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
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String title;
    private String addr;
    private String tel;
    private String image;
    private Double mapX;
    private Double mapY;

    @Column(name = "dataType")
    private int dataType = 5;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    @Column(name = "like_count")
    private int likeCount = 0;

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    @JsonGetter("countLikes")
    public int getLikeCount() {
        return this.likeCount;
    }

    public Restaurant(String title, String addr, String tel, String image, Double mapX, Double mapY) {
        this.title = title;
        this.addr = addr;
        this.tel = tel;
        this.image = image;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public Restaurant() {
    }
}
