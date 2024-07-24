package goorm.dofarming.infra.tourapi.repository;

import goorm.dofarming.infra.tourapi.domain.Ocean;
import goorm.dofarming.infra.tourapi.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query("SELECT o FROM Tour o WHERE "
            + "(6371 * acos(cos(radians(:mapY)) * cos(radians(o.mapY)) "
            + "* cos(radians(o.mapX) - radians(:mapX)) + sin(radians(:mapY)) "
            + "* sin(radians(o.mapY)))) <= :radius")
    List<Tour> findAllByDistance(
            @Param("mapX") double mapX,
            @Param("mapY") double mapY,
            @Param("radius") double radius
    );
}
