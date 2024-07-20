package goorm.dofarming.domain.jpa.tag.repository;

import goorm.dofarming.domain.jpa.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
