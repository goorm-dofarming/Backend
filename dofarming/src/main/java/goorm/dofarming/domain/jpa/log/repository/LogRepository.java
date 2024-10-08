package goorm.dofarming.domain.jpa.log.repository;

import goorm.dofarming.domain.jpa.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {
    @Query(value = "SELECT * FROM log l WHERE l.user_id = :userId ORDER BY l.log_id DESC LIMIT 100", nativeQuery = true)
    List<Log> find100ByUser_UserId(Long userId); // 최신화해서 50개까지 조회
}
