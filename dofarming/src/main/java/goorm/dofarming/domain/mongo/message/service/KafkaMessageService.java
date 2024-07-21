package goorm.dofarming.domain.mongo.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.dofarming.domain.mongo.message.dto.MessageDto;
import goorm.dofarming.domain.mongo.message.entity.Message;
import goorm.dofarming.domain.mongo.message.repository.MessageRepository;
import goorm.dofarming.global.config.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageService {

    private final MessageRepository messageRepository;

    private final ObjectMapper mapper;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = "message-group")
    public void saveMessage(String message) throws IOException {
        MessageDto messageDto = mapper.readValue(message, MessageDto.class);
        messageRepository.save(Message.message(messageDto));
    }
}
