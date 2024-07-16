package goorm.chat.service;

import goorm.chat.domain.Message;
import goorm.chat.dto.MessageRequest;
import goorm.chat.dto.MessageResponse;
import goorm.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public MessageResponse saveMessage(Long roomId, MessageRequest messageRequest) {
        Message message = Message.message(roomId, messageRequest);
        Message saveMessage = messageRepository.save(message);
        return MessageResponse.from(saveMessage);
    }
}
