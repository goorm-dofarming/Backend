package goorm.dofarming.domain.jpa.review.service;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.domain.jpa.image.service.ImageService;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.review.entity.ReviewDTO;
import goorm.dofarming.domain.jpa.review.repository.ReviewRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.Ocean;
import goorm.dofarming.infra.tourapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final OceanRepository oceanRepository;
    private final MountainRepository mountainRepository;
    private final ActivityRepository activityRepository;
    private final TourRepository tourRepository;
    private final RestaurantRepository restaurantRepository;
    private final CafeRepository cafeRepository;
    private final ImageService imageService;

    @Transactional
    public ReviewDTO createReview(List<MultipartFile> files, Long userId, Long locationId, Double score, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 장소입니다."));

        Review review = new Review();
        review.setReviewId(review.getReviewId());
        review.setContent(content);
        review.setScore(score);
        review.addUser(user);
        review.addLocation(location);

        // Review 객체를 먼저 저장
        Review savedReview = reviewRepository.save(review);

        List<String> images = new ArrayList<>();

        for (MultipartFile file : files) {
            String imageUrl = imageService.uploadFile(file);
            images.add(imageUrl); // 반환할 이미지 리스트 저장
            Image image = new Image();
            image.setImageUrl(imageUrl);
            image.setReview(savedReview); // 연관관계 설정
            imageRepository.save(image);
        }

        return buildReviewDTO(savedReview, images);
    }

    // 한 리뷰에 대해 사진만 띄우는 기능
    public List<String> getImageUrls(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 리뷰입니다."));
        List<String> imageList = new ArrayList<>();
        for (Image image : review.getImages()) {
            String imageUrl = image.getImageUrl();
            imageList.add(imageUrl);
        }
        return imageList;
    }

    private ReviewDTO buildReviewDTO(Review review, List<String> imageUrls) {

        return new ReviewDTO(
                review.getReviewId(),
                review.getScore(),
                review.getContent(),
                imageUrls
        );
    }
}

