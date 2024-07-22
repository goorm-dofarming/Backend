
package goorm.dofarming.infra.tourapi.repository;

import goorm.dofarming.infra.tourapi.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
