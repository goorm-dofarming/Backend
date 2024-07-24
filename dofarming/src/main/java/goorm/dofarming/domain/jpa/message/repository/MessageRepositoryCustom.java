package goorm.dofarming.domain.jpa.message.repository;

import goorm.dofarming.domain.jpa.message.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepositoryCustom {
    List<Message> search(Long messageId, Long roomId, LocalDateTime createdAt);
}
