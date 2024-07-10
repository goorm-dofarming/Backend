package goorm.dofarming.domain.jpa.user.repository;

import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByUserIdAndStatus(Long userId, Status status);
}
