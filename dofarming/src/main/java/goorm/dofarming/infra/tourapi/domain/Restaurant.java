package goorm.dofarming.infra.tourapi.domain;

import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String addr;
    private String tel;
    private String image;
    private Double mapX;
    private Double mapY;

    @Column(name = "dataType")
    private int dataType = 5;

//    @Enumerated(EnumType.STRING)
//    private Status status = Status.ACTIVE;

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
