package goorm.dofarming.domain.mongo.message.repository;

import goorm.dofarming.domain.mongo.message.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String>, MessageRepositoryCustom {
}
