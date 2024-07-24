package goorm.dofarming.domain.jpa.message.entity;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.message.dto.MessageDto;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    private String nickname;

    private MessageType messageType;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "join_id")
    private Join join;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Chatroom chatroom;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Join> readJoins = new ArrayList<>();

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
