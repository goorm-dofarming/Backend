package goorm.dofarming.domain.mongo.message.service;

import goorm.dofarming.domain.mongo.message.dto.request.MessageSearchRequest;
import goorm.dofarming.domain.mongo.message.dto.response.MessageResponse;
import goorm.dofarming.domain.mongo.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<MessageResponse> searchMessageList(MessageSearchRequest messageSearchRequest) {
        return messageRepository.search(messageSearchRequest)
                .stream().map(MessageResponse::from).collect(Collectors.toList());
    }
}
