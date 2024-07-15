package goorm.dofarming.domain.jpa.chatroom.service;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomCreateRequest;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.chatroom.repository.ChatroomRepository;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.join.repository.JoinRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final JoinRepository joinRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createRoom(ChatroomCreateRequest chatroomCreateRequest) {
        Chatroom chatroom = Chatroom.chatroom(chatroomCreateRequest.title(), chatroomCreateRequest.region());
        Chatroom saveChatroom = chatroomRepository.save(chatroom);
        return saveChatroom.getRoomId();
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Chatroom chatroom = existByRoomId(roomId);
        chatroom.delete();
    }

    @Transactional
    public Long joinRoom(Long userId, Long roomId) {
        User user = existByUserId(userId);
        Chatroom chatroom = existByRoomId(roomId);

        Join join = Join.join(user, chatroom);
        Join saveJoin = joinRepository.save(join);
        return saveJoin.getJoinId();
    }

    @Transactional
    public void leaveRoom(Long userId, Long roomId) {
        Join join = joinRepository.findByUser_UserIdAndChatroom_RoomIdAndStatus(userId, roomId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "입장한 채팅방이 아닙니다."));
        join.delete();
    }

    private Chatroom existByRoomId(Long roomId) {
        return chatroomRepository.findByRoomIdAndStatus(roomId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Room not found."));
    }

    private User existByUserId(Long userId) {
        return userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "User not found."));
    }
}
