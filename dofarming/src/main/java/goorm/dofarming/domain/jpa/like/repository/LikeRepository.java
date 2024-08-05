package goorm.dofarming.domain.jpa.like.repository;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
    List<Like> findAllByUser_UserIdAndStatus(Long userId, Status status);

    Optional<Like> findByUser_UserIdAndLocation_LocationId(Long userId, Long locationId);

    boolean existsByLocation_LocationIdAndStatus(Long locationId, Status status);

    boolean existsByLocation_LocationIdAndUser_UserIdAndStatus(Long locationId, Long userId, Status status);
}
