package goorm.dofarming.domain.jpa.join.repository;

import goorm.dofarming.domain.jpa.join.entity.Join;

import java.time.LocalDateTime;
import java.util.List;

public interface JoinRepositoryCustom {
    List<Join> search(Long userId, String condition);
}
