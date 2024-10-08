package goorm.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.chat.config.KafkaConstants;
import goorm.chat.dto.MessageDto;
import goorm.chat.dto.MessageResponse;
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

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ObjectMapper mapper;

    public void send(MessageDto message) {
        try {
            final String payload = mapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaConstants.CHAT_TOPIC, payload);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = KafkaConstants.PROCESS_CHAT_TOPIC)
    public void consume(String message) throws IOException {
        try {
            MessageResponse messageResponse = mapper.readValue(message, MessageResponse.class);
            simpMessagingTemplate.convertAndSend("/room/" + messageResponse.roomId(), messageResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
