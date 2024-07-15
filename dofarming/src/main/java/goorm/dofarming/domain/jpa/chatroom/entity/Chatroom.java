package goorm.dofarming.domain.jpa.chatroom.entity;

import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.tag.entity.Tag;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


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
        this.tags.stream().forEach(Tag::delete);
    }
}
