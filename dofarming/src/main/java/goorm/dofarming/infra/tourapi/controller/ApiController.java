package goorm.dofarming.infra.tourapi.controller;

import goorm.dofarming.infra.tourapi.service.DownloadApiService;
import goorm.dofarming.infra.tourapi.service.testpackage.TestApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apiCall")
public class ApiController {

    private final TestApiService testApiService;
    private final DownloadApiService downloadApiService;

    @GetMapping("/testApi")
    public String testApi() {
        return testApiService.DownloadRestaurantDataByCat3();
    }

    @GetMapping("/download/Restaurant")
    public String updateRestaurant() {
        return downloadApiService.DownloadRestaurantListByCat3();
    }

    @GetMapping("/download/Activity")
    public String updateActivity() {
        return downloadApiService.DownloadActivityListByCat3();
    }

    @GetMapping("/download/TourAttraction")
    public String updateTourAttraction() {
        return downloadApiService.DownloadTourListByCat3();
    }

    @GetMapping("/download/Cafe")
    public String updateCafe() {
        return downloadApiService.DownloadCafeListByCat3();
    }

    @GetMapping("/download/Ocean")
    public String updateOcean() {
        return downloadApiService.DownloadOceanListByCat3();
    }

    @GetMapping("/download/Mountain")
    public String updateMountain() {
        return downloadApiService.DownloadMountainListByCat3();
    }

}
