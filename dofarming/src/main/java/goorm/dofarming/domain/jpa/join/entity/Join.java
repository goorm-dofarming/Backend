package goorm.dofarming.domain.jpa.join.entity;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.BaseEntity;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Join extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "join_id")
    private Long joinId;

    private LocalDateTime lastVisit;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    //== 생성 메서드 ==//
    public static Join join(User user, Chatroom chatroom) {

        joinValidateDuplicate(user, chatroom);

        Join join = new Join();
        join.addUser(user);
        join.addChatroom(chatroom);
        join.status = Status.ACTIVE;
        return join;
    }

    //== 연관관계 메서드 ==//
    public void addUser(User user) {
        this.user = user;
        user.getJoins().add(this);
    }

    public void addChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
        chatroom.getJoins().add(this);
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
    }

    public void lastVisitUpdate() {
        this.lastVisit = LocalDateTime.now();
    }

    //== 중복 검증 메서드 ==//
    private static void joinValidateDuplicate(User user, Chatroom chatroom) {
        if (user.getJoins().stream().anyMatch(join -> join.getChatroom().equals(chatroom))) {

        }
    }
}
