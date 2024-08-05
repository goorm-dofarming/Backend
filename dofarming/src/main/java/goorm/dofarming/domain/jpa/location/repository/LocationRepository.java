package goorm.dofarming.domain.jpa.location.repository;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.global.common.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByLocationIdAndStatus(Long locationId, Status status);
}
