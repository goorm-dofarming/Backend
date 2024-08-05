package goorm.dofarming.domain.jpa.image.controller;

import goorm.dofarming.domain.jpa.image.service.ImageService;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @Parameter @PathVariable Long imageId
    ) {
        imageService.delete(imageId);
        return ResponseEntity.noContent().build();
    }
}
