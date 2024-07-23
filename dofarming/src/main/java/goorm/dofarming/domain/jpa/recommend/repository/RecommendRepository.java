package goorm.dofarming.domain.jpa.recommend.repository;

import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}
