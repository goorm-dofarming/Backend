package goorm.dofarming.domain.jpa.recommend.controller;

import goorm.dofarming.domain.jpa.recommend.entity.RecommendDTO;
import goorm.dofarming.domain.jpa.recommend.service.RecommendService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
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
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return recommendService.recommendOcean(userId);
    }

    @GetMapping("/randomTest")
    public RecommendDTO recommendTest(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return recommendService.recommendRandomForUserTest(mapX, mapY, userId);
    }

    @GetMapping("/mountain")
    public List<?> recommendMountain(@AuthenticationPrincipal DofarmingUserDetails user) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
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
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
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
