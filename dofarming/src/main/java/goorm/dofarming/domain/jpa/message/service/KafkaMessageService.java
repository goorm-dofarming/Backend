package goorm.dofarming.domain.jpa.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.chatroom.repository.ChatroomRepository;
import goorm.dofarming.domain.jpa.chatroom.service.ChatroomService;
import goorm.dofarming.domain.jpa.join.dto.request.AlarmDto;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.join.repository.JoinRepository;
import goorm.dofarming.domain.jpa.message.dto.MessageDto;
import goorm.dofarming.domain.jpa.message.dto.response.MessageResponse;
import goorm.dofarming.domain.jpa.message.entity.Message;
import goorm.dofarming.domain.jpa.message.entity.MessageType;
import goorm.dofarming.domain.jpa.message.repository.MessageRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatroomService chatroomService;
    private final ChatroomRepository chatroomRepository;

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

        User findUser = userRepository.findByUserIdAndStatus(messageDto.userId(), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        Chatroom findRoom = chatroomRepository.findByRoomIdAndStatus(messageDto.roomId(), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Room not found"));

        Join findJoin = joinRepository.findByUser_UserIdAndChatroom_RoomIdAndStatus(findUser.getUserId(), findRoom.getRoomId(), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Join not found"));

        Message createMessage = Message.message(findJoin, findRoom, messageDto.nickname(), messageDto.messageType(), messageDto.content());
        messageRepository.save(createMessage);
        send(MessageResponse.from(createMessage));

        if (messageDto.messageType().equals(MessageType.LEAVE)) {
            chatroomService.leaveRoom(findJoin);
        }
    }
}
