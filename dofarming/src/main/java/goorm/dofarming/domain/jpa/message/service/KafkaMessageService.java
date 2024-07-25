package goorm.dofarming.domain.jpa.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.dofarming.domain.jpa.chatroom.repository.ChatroomRepository;
import goorm.dofarming.domain.jpa.join.dto.request.AlarmDto;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.join.repository.JoinRepository;
import goorm.dofarming.domain.jpa.message.dto.MessageDto;
import goorm.dofarming.domain.jpa.message.dto.response.MessageResponse;
import goorm.dofarming.domain.jpa.message.entity.Message;
import goorm.dofarming.domain.jpa.message.repository.MessageRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.global.config.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageService {

    private final JoinRepository joinRepository;
    private final MessageRepository messageRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public void send(MessageResponse message) {
        try {
            final String payload = mapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaConstants.PROCESS_CHAT_TOPIC, payload);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @KafkaListener(topics = KafkaConstants.CHAT_TOPIC, groupId = "message-group")
    public void messagingProcess(String message) throws IOException {
        MessageDto messageDto = mapper.readValue(message, MessageDto.class);

        Join findJoin = joinRepository.findByUser_UserIdAndChatroom_RoomIdAndStatus(messageDto.userId(), messageDto.roomId(), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Join not found"));

        Message createMessage = Message.message(findJoin, messageDto.nickname(), messageDto.messageType(), messageDto.content());
        messageRepository.save(createMessage);

        send(MessageResponse.from(createMessage));
    }
}
