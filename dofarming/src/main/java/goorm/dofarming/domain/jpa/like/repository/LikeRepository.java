package goorm.dofarming.domain.jpa.like.repository;

import goorm.dofarming.domain.jpa.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
