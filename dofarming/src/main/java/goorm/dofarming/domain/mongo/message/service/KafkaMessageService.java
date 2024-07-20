package goorm.dofarming.domain.mongo.message.service;

import goorm.dofarming.domain.mongo.message.dto.MessageDto;
import goorm.dofarming.domain.mongo.message.entity.Message;
import goorm.dofarming.domain.mongo.message.repository.MessageRepository;
import goorm.dofarming.global.config.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {

    private final MessageRepository messageRepository;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = "message-group")
    public void saveMessage(MessageDto messageDto) throws IOException {
        messageRepository.save(Message.message(messageDto));
    }
}
