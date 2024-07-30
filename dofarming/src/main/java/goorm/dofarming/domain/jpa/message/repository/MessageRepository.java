package goorm.dofarming.domain.jpa.message.repository;

import goorm.dofarming.domain.jpa.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {
}
