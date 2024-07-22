package goorm.dofarming.infra.tourapi.repository;

import goorm.dofarming.infra.tourapi.domain.Ocean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OceanRepository extends JpaRepository<Ocean, Long> {
}
