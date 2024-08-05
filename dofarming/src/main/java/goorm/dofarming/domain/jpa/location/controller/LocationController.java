package goorm.dofarming.domain.jpa.location.controller;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.service.LocationService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/location/{locationId}")
    public ResponseEntity<LocationResponse> getLocation(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @PathVariable Long locationId
    ) {
        return ResponseEntity.ok().body(locationService.getLocation(user.getUserId(), locationId));
    }
}
