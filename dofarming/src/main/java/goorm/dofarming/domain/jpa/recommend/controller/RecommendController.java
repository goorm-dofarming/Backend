package goorm.dofarming.domain.jpa.recommend.controller;

import goorm.dofarming.domain.jpa.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    // 제대로 잘 됌.
    @GetMapping("/withOcean")
    public List<?> recommendWithOcean(@RequestParam("themes") List<Integer> themes) {
        return recommendService.recommendWithOcean(themes);
    }

    @GetMapping("/withoutOcean")
    public List<?> recommendWithoutOcean(
            @RequestParam("themes") List<Integer> themes,
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY
    ) {
        return recommendService.recommendWithoutOcean(themes, mapX, mapY);
    }

    @GetMapping("/withoutTheme")
    public List<?> recommendWithoutTheme(
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY
    ) {
        return recommendService.recommendRandom(mapX, mapY);
    }
}
