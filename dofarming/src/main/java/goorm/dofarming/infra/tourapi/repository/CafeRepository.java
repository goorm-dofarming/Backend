package goorm.dofarming.infra.tourapi.repository;

import goorm.dofarming.infra.tourapi.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
}
