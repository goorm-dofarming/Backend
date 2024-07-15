package goorm.dofarming.domain.jpa.tag.service;

import goorm.dofarming.domain.jpa.chatroom.entity.Chatroom;
import goorm.dofarming.domain.jpa.tag.dto.request.TagRequest;
import goorm.dofarming.domain.jpa.tag.entity.Tag;
import goorm.dofarming.domain.jpa.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public void createTag(TagRequest tagRequest, Chatroom chatroom) {
        Tag tag = Tag.tag(tagRequest.name(), tagRequest.color(), chatroom);

        tagRepository.save(tag);
    }
}
