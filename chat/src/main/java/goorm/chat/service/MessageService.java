package goorm.chat.service;

import goorm.chat.config.KafkaConstants;
import goorm.chat.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void send(Object message) {
        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message);
    }

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC)
    public void consume(MessageDto messageDto) throws IOException {
        simpMessagingTemplate.convertAndSend("/room/" + messageDto.roomId(), messageDto);
    }
}
