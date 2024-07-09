package goorm.dofarming.domain.mongo.message.entity;

import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "messages")
public class Message extends BaseEntity {

    @Id
    private String id;

    private Long userId;

    private Long roomId;

    private String content;

    //== 생성 메서드 ==//
    public static Message message(Long userId, Long roomId, String content) {
        Message message = new Message();
        message.userId = userId;
        message.roomId = roomId;
        message.content = content;
        return message;
    }
}
