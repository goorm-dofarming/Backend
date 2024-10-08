package goorm.dofarming.domain.jpa.chatroom.service;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomCreateRequest;
import goorm.dofarming.domain.jpa.chatroom.dto.response.OpenChatroomResponse;
import goorm.dofarming.domain.jpa.chatroom.dto.response.MyChatroomResponse;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.chatroom.repository.ChatroomRepository;

import goorm.dofarming.domain.jpa.tag.entity.Tag;
import goorm.dofarming.domain.jpa.tag.repository.TagRepository;

import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.join.repository.JoinRepository;

import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.global.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final TagRepository tagRepository;
    private final JoinRepository joinRepository;
    private final UserRepository userRepository;

    /**
     * 채팅방 생성
     */
    @Transactional
    public Long createRoom(Long userId, ChatroomCreateRequest chatroomCreateRequest) {
        User user = existByUserId(userId);

        Chatroom chatroom = Chatroom.chatroom(chatroomCreateRequest.title(), chatroomCreateRequest.region());
        Chatroom saveChatroom = chatroomRepository.save(chatroom);

        List<String> tagNames = chatroomCreateRequest.tagNames();
        List<String> randomColor = RandomGenerator.selectRandomColors(tagNames.size());

        for (int i = 0; i < tagNames.size(); i++) {
            Tag tag = Tag.tag(chatroomCreateRequest.tagNames().get(i), randomColor.get(i), chatroom);
            tagRepository.save(tag);
        }

        Join join = Join.join(user, saveChatroom);
        joinRepository.save(join);

        return saveChatroom.getRoomId();
    }

    /**
     * 채팅방 삭제
     */
    @Transactional
    public void deleteRoom(Long roomId) {
        Chatroom chatroom = existByRoomId(roomId);
        chatroom.delete();
    }

    /**
     * 채팅방 입장
     */
    @Transactional
    public Long joinRoom(Long userId, Long roomId) {
        User user = existByUserId(userId);
        Chatroom chatroom = existByRoomId(roomId);

        Join join = Join.join(user, chatroom);
        Join saveJoin = joinRepository.save(join);
        return saveJoin.getJoinId();
    }

    /**
     * 채팅방 퇴장
     */
    @Transactional
    public void leaveRoom(Join join) {
        join.delete();
    }

    /**
     * 오픈 채팅방 검색
     */
    public List<OpenChatroomResponse> searchRoomList(Long userId, Long roomId, String condition, LocalDateTime createdAt) {
        return chatroomRepository.search(roomId, condition, createdAt)
                .stream()
                .filter(chatroom -> chatroom.getJoins().stream().noneMatch(join -> join.getUser().getUserId() == userId))
                .map(OpenChatroomResponse::of).collect(Collectors.toList());
    }

    /**
     * 내 채팅방 리스트
     */
    public List<MyChatroomResponse> myRoomList(Long userId, String condition) {
        return joinRepository.search(userId, condition)
                .stream().map(join -> MyChatroomResponse.of(join)).collect(Collectors.toList());
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