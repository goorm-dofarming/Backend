package goorm.dofarming.domain.jpa.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.FetchType.LAZY;

@Data
@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private Review review;

    public Image() {
    }

}
