package goorm.dofarming.domain.jpa.chatroom.repository;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long>, ChatroomRepositoryCustom {

    Optional<Chatroom> findByRoomIdAndStatus(Long roomId, Status status);
}
