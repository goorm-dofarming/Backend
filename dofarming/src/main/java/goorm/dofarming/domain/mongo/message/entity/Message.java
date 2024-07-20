package goorm.dofarming.domain.mongo.message.entity;

import goorm.dofarming.domain.mongo.message.dto.MessageDto;
import goorm.dofarming.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "messages")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    private Long id;

    private Long userId;

    private String nickname;

    private Long roomId;

    private MessageType messageType;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Message message(MessageDto messageDto) {
        Message message = new Message();
        message.userId = messageDto.userId();
        message.nickname = messageDto.nickname();
        message.messageType = messageDto.messageType();
        message.content = messageDto.content();
        return message;
    }
}
