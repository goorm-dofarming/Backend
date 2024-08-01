package goorm.dofarming.domain.jpa.image.repository;

import goorm.dofarming.domain.jpa.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
