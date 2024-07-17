package goorm.dofarming.infra.controller;

import goorm.dofarming.infra.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @GetMapping("/apiCall")
    public Mono<String> fetchApi(
        @RequestParam double mapX,
        @RequestParam double mapY
    ) {
        return apiService.fetchLocationBasedList(mapX, mapY);
    }
}
