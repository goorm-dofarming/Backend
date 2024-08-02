package goorm.dofarming.domain.jpa.like.repository;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.infra.tourapi.domain.DataType;

import java.time.LocalDateTime;
import java.util.List;

public interface LikeRepositoryCustom {

    List<Like> search(Long userId, Long likeId, LocalDateTime updatedAt, List<String> themes, List<Region> regions, SortType sortType);
}
