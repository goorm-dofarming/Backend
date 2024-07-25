package goorm.dofarming.infra.tourapi.domain;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Ocean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String addr;
    private String tel;
    private String image;
    private Double mapX;
    private Double mapY;

    @Column(name = "data_type")
    private int dataType = 1;

    @OneToMany(mappedBy = "ocean", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    public int countLikes() {
        return likes.size();
    }

//    @Enumerated(EnumType.STRING)
//    private Status status = Status.ACTIVE;

    public Ocean(String title, String addr, String tel, String image, Double mapX, Double mapY) {
        this.title = title;
        this.addr = addr;
        this.tel = tel;
        this.image = image;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public Ocean() {
    }
}
