package goorm.dofarming.domain.jpa.tag.entity;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
        isDuplicateTag(name, chatroom);

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

    private static void isDuplicateTag(String name, Chatroom chatroom) {
        if (chatroom.getTags().stream()
                .filter(tag -> tag.status.equals(Status.ACTIVE))
                .anyMatch(tag -> tag.name.equals(name))) {
            throw new CustomException(ErrorCode.DUPLICATE_OBJECT, "이미 존재하는 Tag 입니다.");
        }
    }

    //== equals(), hashCode() 재정의 ==//
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(getTagId(), tag.getTagId()) && Objects.equals(getName(), tag.getName()) && Objects.equals(getColor(), tag.getColor()) && getStatus() == tag.getStatus() && Objects.equals(getChatroom(), tag.getChatroom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getStatus(), getChatroom());
    }
}
