package goorm.dofarming.infra.tourapi.controller;

import goorm.dofarming.infra.tourapi.domain.Restaurant;
import goorm.dofarming.infra.tourapi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RestaurantRepository restaurantRepository;

    @GetMapping("/apiCall/testGetMapping")
    public List<Restaurant> getRestaurant() {
        return restaurantRepository.findAllById(Arrays.asList(1L,2L,3L));
    }
}
