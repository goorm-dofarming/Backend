package goorm.dofarming.domain.jpa.message.entity;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.global.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "메시지 엔티티")
public class Message extends BaseEntity {

    @Schema(description = "메시지 ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Schema(description = "닉네임", example = "usernickname")
    private String nickname;

    @Schema(description = "메시지 타입", example = "SEND")
    private MessageType messageType;

    @Schema(description = "메시지 내용", example = "Hello!")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "join_id")
    private Join join;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Chatroom chatroom;

    public static Message message(Join join, String nickname, MessageType messageType, String content) {
        Message message = new Message();
        message.addJoin(join);
        message.addMessage(join);
        message.nickname = nickname;
        message.messageType = messageType;
        message.content = content;
        return message;
    }

    //== 연관관계 메서드 ==//
    public void addJoin(Join join) {
        this.join = join;
        join.getMessages().add(this);
    }

    public void addMessage(Join join) {
        this.chatroom = join.getChatroom();
        join.getChatroom().getMessages().add(this);
    }
}
