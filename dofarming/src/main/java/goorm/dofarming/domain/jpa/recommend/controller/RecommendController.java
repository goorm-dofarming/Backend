package goorm.dofarming.domain.jpa.recommend.controller;

import goorm.dofarming.domain.jpa.recommend.entity.RecommendDTO;
import goorm.dofarming.domain.jpa.recommend.service.RecommendService;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.ErrorResponse;
import goorm.dofarming.global.common.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recommend", description = "Recommend 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendService recommendService;
    private final UserRepository userRepository;

    @Operation(
            operationId = "recommendOcean",
            summary = "바다 테마 추천 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendDTO.class), examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"logId\": 196,\n" +
                                            "  \"address\": \"경상남도 남해군 남면\",\n" +
                                            "  \"recommendations\": [\n" +
                                            "    {\n" +
                                            "      \"id\": 73,\n" +
                                            "      \"title\": \"남해 가천 해변과 암수바위\",\n" +
                                            "      \"addr\": \"경상남도 남해군 남면\",\n" +
                                            "      \"tel\": \"\",\n" +
                                            "      \"image\": \"\",\n" +
                                            "      \"mapX\": 127.8940102692,\n" +
                                            "      \"mapY\": 34.72648889,\n" +
                                            "      \"dataType\": 1,\n" +
                                            "      \"liked\": false,\n" +
                                            "      \"countLikes\": 0\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"id\": 5750,\n" +
                                            "      \"title\": \"남해바다체험센터\",\n" +
                                            "      \"addr\": \"경상남도 남해군 남면 남면로1229번길 10-34\",\n" +
                                            "      \"tel\": \"\",\n" +
                                            "      \"image\": \"http://tong.visitkorea.or.kr/cms/resource/31/2787731_image2_1.jpg\",\n" +
                                            "      \"mapX\": 127.861079344,\n" +
                                            "      \"mapY\": 34.7448168207,\n" +
                                            "      \"dataType\": 3,\n" +
                                            "      \"liked\": false,\n" +
                                            "      \"countLikes\": 0\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            ))
                    }),
                    @ApiResponse(responseCode = "404", description = "회원정보가 일치하지 않음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/ocean")
    public RecommendDTO recommendOcean(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        return recommendService.recommendOcean(user.getUserId());
    }

    @Operation(
            operationId = "recommendMountain",
            summary = "산 테마 추천 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendDTO.class), examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"logId\": 198,\n" +
                                            "  \"address\": \"경상남도\",\n" +
                                            "  \"recommendations\": [\n" +
                                            "    {\n" +
                                            "      \"id\": 150,\n" +
                                            "      \"title\": \"지리산 국립공원\",\n" +
                                            "      \"addr\": \"경상남도 함양군 마천면\",\n" +
                                            "      \"tel\": \"\",\n" +
                                            "      \"image\": \"\",\n" +
                                            "      \"mapX\": 127.625633,\n" +
                                            "      \"mapY\": 35.354141,\n" +
                                            "      \"dataType\": 2,\n" +
                                            "      \"liked\": false,\n" +
                                            "      \"countLikes\": 0\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            ))
                    }),
                    @ApiResponse(responseCode = "404", description = "회원정보가 일치하지 않음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/mountain")
    public RecommendDTO recommendMountain(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        return recommendService.recommendMountain(user.getUserId());
    }

    @Operation(
            operationId = "recommendTheme",
            summary = "테마별 추천 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendDTO.class), examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"logId\": 198,\n" +
                                            "  \"address\": \"address\",\n" +
                                            "  \"recommendations\": [\n" +
                                            "    {\n" +
                                            "      \"id\": 3464,\n" +
                                            "      \"title\": \"국립암\",\n" +
                                            "      \"addr\": \"전라남도 영광군 영광읍 대학길4길 10\",\n" +
                                            "      \"tel\": \"061-351-2020\",\n" +
                                            "      \"image\": \"http://tong.visitkorea.or.kr/cms/resource/45/1972345_image2_1.jpg\",\n" +
                                            "      \"mapX\": 126.4976638514,\n" +
                                            "      \"mapY\": 35.2758643528,\n" +
                                            "      \"dataType\": 5,\n" +
                                            "      \"liked\": false,\n" +
                                            "      \"countLikes\": 0\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            ))
                    }),
                    @ApiResponse(responseCode = "404", description = "회원정보가 일치하지 않음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/theme")
    public RecommendDTO recommendTheme(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "테마 유형") @RequestParam("theme") int dataType,
            @Parameter(description = "위도") @RequestParam("mapX") double mapX,
            @Parameter(description = "경도") @RequestParam("mapY") double mapY,
            @Parameter(description = "주소") @RequestParam("address") String address
    ) {
        return recommendService.recommendTheme(dataType, mapX, mapY, user.getUserId(), address);
    }

    @Operation(
            operationId = "recommendRandom",
            summary = "랜덤 추천 API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리됨", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendDTO.class), examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"logId\": 199,\n" +
                                            "  \"address\": \"address\",\n" +
                                            "  \"recommendations\": [\n" +
                                            "    {\n" +
                                            "      \"id\": 672,\n" +
                                            "      \"title\": \"영광 융암마을\",\n" +
                                            "      \"addr\": \"전라남도 영광군 군남면 육창로1길 50-1\",\n" +
                                            "      \"tel\": \"\",\n" +
                                            "      \"image\": \"\",\n" +
                                            "      \"mapX\": 126.4344547522,\n" +
                                            "      \"mapY\": 35.196750576,\n" +
                                            "      \"dataType\": 3,\n" +
                                            "      \"liked\": true,\n" +
                                            "      \"countLikes\": 1\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            ))
                    }),
                    @ApiResponse(responseCode = "404", description = "회원정보가 일치하지 않음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping("/random")
    public RecommendDTO recommendRandom(
            @Parameter(description = "위도") @RequestParam("mapX") double mapX,
            @Parameter(description = "경도") @RequestParam("mapY") double mapY,
            @Parameter(description = "주소") @RequestParam("address") String address,
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        if (user == null) {
            return recommendService.recommendRandomForGuest(mapX, mapY, address);
        }

        return userRepository.findByUserIdAndStatus(user.getUserId(), Status.ACTIVE)
                .map(findUser -> recommendService.recommendRandomForUser(mapX, mapY, findUser.getUserId(), address))
                .orElseGet(() -> recommendService.recommendRandomForGuest(mapX, mapY, address));
    }
}
