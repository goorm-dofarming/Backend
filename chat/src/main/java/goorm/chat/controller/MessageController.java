package goorm.chat.controller;

import goorm.chat.dto.MessageDto;
import goorm.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessageDto messageDto) {
        messageService.send(messageDto);
    }
}
