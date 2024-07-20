package goorm.dofarming.domain.mongo.message.service;

import goorm.dofarming.domain.mongo.message.dto.response.MessageResponse;
import goorm.dofarming.domain.mongo.message.repository.MessageRepository;
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

    /**
     * 메시지 조회
     */
    public List<MessageResponse> searchMessageList(Long messageId, Long roomId, LocalDateTime createdAt) {
        return messageRepository.search(messageId, roomId, createdAt)
                .stream().map(MessageResponse::from).collect(Collectors.toList());
    }
}
