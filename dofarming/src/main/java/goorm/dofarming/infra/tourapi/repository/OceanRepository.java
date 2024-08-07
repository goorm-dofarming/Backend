package goorm.dofarming.infra.tourapi.repository;

import goorm.dofarming.infra.tourapi.domain.Ocean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OceanRepository extends JpaRepository<Ocean, Long> {

    @Query("SELECT o FROM Ocean o WHERE "
            + "(6371 * acos(cos(radians(:mapY)) * cos(radians(o.mapY)) "
            + "* cos(radians(o.mapX) - radians(:mapX)) + sin(radians(:mapY)) "
            + "* sin(radians(o.mapY)))) <= :radius")
    List<Ocean> findAllByDistance(
            @Param("mapX") double mapX,
            @Param("mapY") double mapY,
            @Param("radius") double radius
    );

    @Query(value = "SELECT * FROM location WHERE theme = '1' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Ocean> findRandomOcean();
}
