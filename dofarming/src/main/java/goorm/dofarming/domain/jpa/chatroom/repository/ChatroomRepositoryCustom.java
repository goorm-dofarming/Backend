package goorm.dofarming.domain.jpa.chatroom.repository;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomSearchRequest;
import goorm.dofarming.domain.jpa.chatroom.dto.response.ChatroomResponse;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;

import java.util.List;

public interface ChatroomRepositoryCustom {
    List<Chatroom> search(ChatroomSearchRequest request);
}
