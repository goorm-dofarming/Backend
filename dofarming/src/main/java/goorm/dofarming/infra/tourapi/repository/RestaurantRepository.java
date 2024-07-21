package goorm.dofarming.infra.tourapi.repository;

import goorm.dofarming.infra.tourapi.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
