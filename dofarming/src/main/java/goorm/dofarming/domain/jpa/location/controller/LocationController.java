package goorm.dofarming.domain.jpa.location.controller;

import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.location.service.LocationService;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
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
