package goorm.dofarming.domain.jpa.join.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.dofarming.domain.jpa.join.dto.request.AlarmDto;
import goorm.dofarming.domain.jpa.join.repository.JoinRepository;
import goorm.dofarming.domain.jpa.message.dto.MessageDto;
import goorm.dofarming.domain.jpa.message.entity.MessageType;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.config.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KafkaJoinService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JoinRepository joinRepository;
    private final ObjectMapper mapper;

    public void send(AlarmDto message) {
        try {
            final String payload = mapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaConstants.ALARM_TOPIC, payload);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @KafkaListener(topics = KafkaConstants.CHAT_TOPIC, groupId = "alarm-group")
    public void makeAlarm(String message) throws IOException {
        MessageDto messageDto = mapper.readValue(message, MessageDto.class);

        joinRepository.findAllByChatroom_RoomIdAndStatus(messageDto.roomId(), Status.ACTIVE)
                .stream()
                .forEach(join -> send(AlarmDto.of(join.getUser().getUserId(), messageDto)));
    }
}
