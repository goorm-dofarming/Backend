package goorm.dofarming.domain.jpa.tag.entity;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    private String name;

    private String color;

    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    //== 생성 메서드 ==//
    public static Tag tag(String name, String color, Chatroom chatroom) {
        Tag tag = new Tag();
        tag.name = name;
        tag.color = color;
        tag.addChatroom(chatroom);
        tag.status = Status.ACTIVE;
        return tag;
    }

    //== 연관관계 메서드 ==//
    public void addChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
        chatroom.getTags().add(this);
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
    }
}
