package goorm.dofarming.domain.jpa.location.repository;

import goorm.dofarming.domain.jpa.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
