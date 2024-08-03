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

        Double averageScore = calAverageScore(location);

        return buildReviewDTO(user, savedReview, images, averageScore);
    }

    // 모든 리뷰 받아오기 (처음 장소 클릭했을 때, 실행)
    public List<ReviewDTO> getReviews(Long locationId) {
        List<ReviewDTO> result = new ArrayList<>();

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 장소가 존재하지 않습니다."));

        List<Review> reviews = location.getReviews();

        Double averageScore = calAverageScore(location);

        for (Review review : reviews) {
            List<String> images = new ArrayList<>();
            User user = review.getUser();
            for (Image image : review.getImages()) {
                images.add(image.getImageUrl());
            }
            ReviewDTO reviewDTO = buildReviewDTO(user, review, images, averageScore);
            result.add(reviewDTO);
        }
        return result;
    }

    // 한 리뷰에 대해 사진만 띄우는 기능 - 네이버처럼
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

    public Double calAverageScore(Location location) {
        Double averScore = 0.0;
        if(location.getReviews().isEmpty()) return averScore;

        for (Review review : location.getReviews()) {
            averScore += review.getScore();
        }
        return averScore/location.getReviews().size();
    }

    private ReviewDTO buildReviewDTO(User user, Review review, List<String> imageUrls, Double averScore) {

        return new ReviewDTO(
                review.getReviewId(),
                review.getScore(),
                averScore,
                review.getContent(),
                user.getImageUrl(),
                user.getNickname(),
                imageUrls
        );
    }
}

