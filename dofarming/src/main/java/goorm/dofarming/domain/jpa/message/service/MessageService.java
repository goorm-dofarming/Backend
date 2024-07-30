package goorm.dofarming.domain.jpa.message.service;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.chatroom.repository.ChatroomRepository;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.join.repository.JoinRepository;
import goorm.dofarming.domain.jpa.message.dto.MessageDto;
import goorm.dofarming.domain.jpa.message.dto.response.MessageResponse;
import goorm.dofarming.domain.jpa.message.entity.Message;
import goorm.dofarming.domain.jpa.message.repository.MessageRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final JoinRepository joinRepository;

    /**
     * 메시지 조회
     */
    public List<MessageResponse> searchMessageList(Long userId, Long messageId, Long roomId, LocalDateTime createdAt) {


        Join join = joinRepository.findByUser_UserIdAndChatroom_RoomIdAndStatus(userId, roomId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Join not found."));

        LocalDateTime entryTime = join.getCreatedAt();

        return messageRepository.search(entryTime, join.getLastReadMessageId(), messageId, roomId, createdAt)
                .stream().map(MessageResponse::from).collect(Collectors.toList());
    }
}
