package goorm.dofarming.domain.jpa.chatroom.service;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomCreateRequest;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.chatroom.repository.ChatroomRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    @Transactional
    public Long createRoom(ChatroomCreateRequest chatroomCreateRequest) {
        Chatroom chatroom = Chatroom.chatroom(chatroomCreateRequest.title(), chatroomCreateRequest.region());
        Chatroom saveChatroom = chatroomRepository.save(chatroom);
        return saveChatroom.getRoomId();
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Chatroom chatroom = existByRoomId(roomId);
        if (chatroom.getJoins().stream()
                .filter(findJoin -> findJoin.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList()).size() == 0) {
            chatroom.delete();
        }
    }

    private Chatroom existByRoomId(Long roomId) {
        return chatroomRepository.findByRoomIdAndStatus(roomId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Room not found."));
    }
}
