package goorm.dofarming.domain.jpa.chatroom.repository;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatroomRepositoryCustom {
    List<Chatroom> search(Long roomId, String condition, LocalDateTime createdAt);
}
