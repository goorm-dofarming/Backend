package goorm.dofarming.infra.tourapi.controller;

import goorm.dofarming.infra.tourapi.dto.Item;
import goorm.dofarming.infra.tourapi.service.ContentBasedApiService;
import goorm.dofarming.infra.tourapi.service.LocationBasedApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final LocationBasedApiService locationBasedApiService;
    private final ContentBasedApiService contentBasedApiService;

    @GetMapping("/apiCall")
    public Mono<Map<Integer, List<Item>>> fetchLocation(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam List<Integer> contentTypeIds
    ) {
        return locationBasedApiService.fetchLocationBasedLists(mapX, mapY, contentTypeIds);
    }

    @GetMapping("/apiCall/mountains")
    public Mono<List<Item>> fetchMountains(
            @RequestParam double mapX,
            @RequestParam double mapY
    ) {
        return contentBasedApiService.fetchMountains(mapX, mapY);
    }
}
