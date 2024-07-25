package goorm.dofarming.domain.jpa.recommend.controller;

import goorm.dofarming.domain.jpa.recommend.service.RecommendService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/ocean")
    public List<?> recommendOcean(@AuthenticationPrincipal DofarmingUserDetails user) {
        Long userId = user.getUserId();
        return recommendService.recommendOcean(userId);
    }

    @GetMapping("/mountain")
    public List<?> recommendMountain(@AuthenticationPrincipal DofarmingUserDetails user) {
        Long userId = user.getUserId();
        return recommendService.recommendMountain(userId);
    }

    @GetMapping("/theme")
    public List<?> recommendTheme(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @RequestParam("theme") int dataType,
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY
    ) {
        Long userId = user.getUserId();
        return recommendService.recommendTheme(dataType, mapX, mapY, userId);
    }

    @GetMapping("/random")
    public List<?> recommendRandom(
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY,
            @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        if(user != null){
            Long userId = user.getUserId();
            return recommendService.recommendRandomForUser(mapX, mapY, userId);
        }
        else {
            return recommendService.recommendRandomForGuest(mapX, mapY);
        }
    }
}
