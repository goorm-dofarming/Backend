package goorm.dofarming.domain.mongo.message.repository;

import goorm.dofarming.domain.mongo.message.dto.request.MessageSearchRequest;
import goorm.dofarming.domain.mongo.message.entity.Message;

import java.util.List;

public interface MessageRepositoryCustom {
    List<Message> search(MessageSearchRequest request);
}
