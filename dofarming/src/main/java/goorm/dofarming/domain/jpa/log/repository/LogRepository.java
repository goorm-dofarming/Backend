package goorm.dofarming.domain.jpa.log.repository;

import goorm.dofarming.domain.jpa.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {
}
