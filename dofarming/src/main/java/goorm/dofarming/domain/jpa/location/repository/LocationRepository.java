package goorm.dofarming.domain.jpa.location.repository;

import goorm.dofarming.domain.jpa.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
