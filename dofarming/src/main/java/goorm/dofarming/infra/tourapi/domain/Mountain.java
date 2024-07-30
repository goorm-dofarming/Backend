package goorm.dofarming.infra.tourapi.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Mountain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mountain_id")
    private Long id;

    private String title;
    private String addr;
    private String tel;
    private String image;
    private Double mapX;
    private Double mapY;

    @Column(name = "dataType")
    private int dataType = 2;

    @Transient
    private boolean isLiked;

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @OneToMany(mappedBy = "mountain", cascade = CascadeType.ALL)
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

    public Mountain(String title, String addr, String tel, String image, Double mapX, Double mapY) {
        this.title = title;
        this.addr = addr;
        this.tel = tel;
        this.image = image;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public Mountain() {
    }
}
