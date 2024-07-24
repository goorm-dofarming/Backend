package goorm.dofarming.domain.jpa.chatroom.entity;

import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.message.entity.Message;
import goorm.dofarming.domain.jpa.tag.entity.Tag;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    private String title;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long participantCount = 0L;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<Join> joins = new ArrayList<>();

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();


    //== 생성 메서드 ==//
    public static Chatroom chatroom(String title, Region region) {
        Chatroom chatroom = new Chatroom();
        chatroom.title = title;
        chatroom.region = region;
        chatroom.status = Status.ACTIVE;
        return chatroom;
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
        this.joins.stream().forEach(Join::delete);
        this.tags.stream().forEach(Tag::delete);
    }

    //== 참가자 수 증가 ==//
    public void increaseCount() {
        this.participantCount += 1;
    }

    public void decreaseCount() {
        this.participantCount -= 1;
        if (participantCount == 0L) {
            this.delete();
        }
    }
}
