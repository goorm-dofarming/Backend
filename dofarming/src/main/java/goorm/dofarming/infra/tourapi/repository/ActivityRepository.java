
package goorm.dofarming.infra.tourapi.repository;

import goorm.dofarming.infra.tourapi.domain.Activity;
import goorm.dofarming.infra.tourapi.domain.Ocean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("SELECT o FROM Activity o WHERE "
//            + "o.status = :status AND"
            + "(6371 * acos(cos(radians(:mapY)) * cos(radians(o.mapY)) "
            + "* cos(radians(o.mapX) - radians(:mapX)) + sin(radians(:mapY)) "
            + "* sin(radians(o.mapY)))) <= :radius")
    List<Activity> findAllByDistance(
            @Param("mapX") double mapX,
            @Param("mapY") double mapY,
            @Param("radius") double radius
    );
}
