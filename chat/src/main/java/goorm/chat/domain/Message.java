package goorm.chat.domain;

import goorm.chat.dto.MessageRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    private String id;

    private Long userId;

    private String nickname;

    private Long roomId;

    private MessageType messageType;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    //== 생성 메서드 ==//
    public static Message message(Long roomId, MessageRequest messageRequest) {
        Message message = new Message();
        message.userId = messageRequest.userId();
        message.nickname = messageRequest.nickname();
        message.roomId = roomId;
        message.messageType = messageRequest.messageType();
        message.content = messageRequest.content();
        return message;
    }
}
