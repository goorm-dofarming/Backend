package goorm.chat.dto;

import goorm.chat.domain.Message;
import goorm.chat.domain.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record MessageRequest(

        @NotEmpty(message = "유저 Id 는 필수 항목입니다.")
        Long userId,
        @NotEmpty(message = "닉네임은 필수 항목입니다.")
        String nickname,
        @NotEmpty(message = "메시지 타입은 필수 항목입니다.")
        MessageType messageType,
        String content
) {
    public static MessageRequest from(Message message) {
        return new MessageRequest(
                message.getUserId(),
                message.getNickname(),
                message.getMessageType(),
                message.getContent()
        );
    }

}
