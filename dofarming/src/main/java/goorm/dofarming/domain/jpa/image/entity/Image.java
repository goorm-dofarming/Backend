package goorm.dofarming.domain.jpa.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.FetchType.LAZY;

@Data
@Entity
public class Image extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public void delete() {
        this.status = Status.DELETE;
    }

    public Image() {
    }

}
