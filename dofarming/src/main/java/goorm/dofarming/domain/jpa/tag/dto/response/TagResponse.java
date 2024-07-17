package goorm.dofarming.domain.jpa.tag.dto.response;

import goorm.dofarming.domain.jpa.chatroom.dto.response.ChatroomResponse;
import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.tag.entity.Tag;

public record TagResponse(
        String name,
        String color
) {

    public static TagResponse of(Tag tag) {
        return new TagResponse(
                tag.getName(),
                tag.getColor()
        );
    }
}
