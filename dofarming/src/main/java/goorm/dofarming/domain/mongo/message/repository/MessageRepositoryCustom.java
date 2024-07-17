package goorm.dofarming.domain.mongo.message.repository;

import goorm.dofarming.domain.mongo.message.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepositoryCustom {
    List<Message> search(Long messageId, Long roomId, LocalDateTime createdAt);
}
