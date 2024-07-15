package goorm.dofarming.domain.jpa.join.repository;

import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinRepository extends JpaRepository<Join, Long> {
    Optional<Join> findByUser_UserIdAndChatroom_RoomIdAndStatus(Long userId, Long roomId, Status status);
}
