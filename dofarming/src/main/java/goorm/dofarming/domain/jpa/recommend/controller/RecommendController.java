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
    public RecommendDTO recommendOcean(@AuthenticationPrincipal DofarmingUserDetails user) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return recommendService.recommendOcean(userId);
    }

    @GetMapping("/mountain")
    public RecommendDTO recommendMountain(@AuthenticationPrincipal DofarmingUserDetails user) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return recommendService.recommendMountain(userId);
    }

    @GetMapping("/theme")
    public RecommendDTO recommendTheme(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @RequestParam("theme") int dataType,
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY,
            @RequestParam("address") String address
    ) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();
        return recommendService.recommendTheme(dataType, mapX, mapY, userId, address);
    }

    @GetMapping("/random")
    public RecommendDTO recommendRandom(
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY,
            @RequestParam("address") String address,
            @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        if(user != null){
            Long userId = user.getUserId();
            return recommendService.recommendRandomForUser(mapX, mapY, userId, address);
        }
        else {
            return recommendService.recommendRandomForGuest(mapX, mapY, address);
        }
    }
}
