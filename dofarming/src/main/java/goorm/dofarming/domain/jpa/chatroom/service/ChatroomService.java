package goorm.dofarming.domain.jpa.chatroom.service;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomCreateRequest;
import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomSearchRequest;
import goorm.dofarming.domain.jpa.chatroom.dto.response.ChatroomResponse;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.chatroom.repository.ChatroomRepository;
import goorm.dofarming.domain.jpa.tag.dto.request.TagRequest;
import goorm.dofarming.domain.jpa.tag.entity.Tag;
import goorm.dofarming.domain.jpa.tag.repository.TagRepository;
import goorm.dofarming.domain.jpa.tag.service.TagService;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final TagRepository tagRepository;

    /**
     * 채팅방 생성
     */
    @Transactional
    public Long createRoom(ChatroomCreateRequest chatroomCreateRequest) {
        Chatroom chatroom = Chatroom.chatroom(chatroomCreateRequest.title(), chatroomCreateRequest.region());
        Chatroom saveChatroom = chatroomRepository.save(chatroom);

        for (TagRequest tagRequest : chatroomCreateRequest.tags()) {
            Tag tag = Tag.tag(tagRequest.name(), tagRequest.color(), chatroom);
            tagRepository.save(tag);
        }

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
     * 오픈 채팅방 검색
     */
    public List<ChatroomResponse> searchRoomList(ChatroomSearchRequest chatroomSearchRequest) {
        return chatroomRepository.search(chatroomSearchRequest)
                .stream().map(ChatroomResponse::of).collect(Collectors.toList());
    }

    private Chatroom existByRoomId(Long roomId) {
        return chatroomRepository.findByRoomIdAndStatus(roomId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Room not found."));
    }
}
