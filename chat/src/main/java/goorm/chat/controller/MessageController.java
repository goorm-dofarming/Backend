package goorm.chat.controller;

import goorm.chat.dto.MessageDto;
import goorm.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/sendMessage")
    public void send(@Payload MessageDto messageDto) {
        messageService.send(messageDto);
    }
}
