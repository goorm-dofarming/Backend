package goorm.dofarming.domain.mongo.message.entity;

import goorm.dofarming.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "messages")
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
    public static Message message(Long userId, Long roomId, String content) {
        Message message = new Message();
        message.userId = userId;
        message.roomId = roomId;
        message.content = content;
        return message;
    }
}
