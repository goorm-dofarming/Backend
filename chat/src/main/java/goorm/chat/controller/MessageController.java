package goorm.chat.controller;

import goorm.chat.dto.MessageRequest;
import goorm.chat.dto.MessageResponse;
import goorm.chat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/room/{roomId}")
    public MessageResponse send(
            @DestinationVariable Long roomId,
            @Valid @Payload MessageRequest messageRequest
    ) {
        return messageService.saveMessage(roomId, messageRequest);
    }
}
