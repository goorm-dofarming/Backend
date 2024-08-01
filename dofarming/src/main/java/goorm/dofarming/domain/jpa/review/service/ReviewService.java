package goorm.dofarming.domain.jpa.review.service;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.domain.jpa.image.service.ImageService;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.review.entity.ReviewDTO;
import goorm.dofarming.domain.jpa.review.repository.ReviewRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public ReviewDTO saveReview(List<MultipartFile> files, Long userId, Double score, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Review review = new Review();
        review.setContent(content);
        review.setScore(score);
        review.setUser(user);

        // Review 객체를 먼저 저장
        Review savedReview = reviewRepository.save(review);

        for (MultipartFile file : files) {
            String imageUrl = imageService.uploadFile(file);
            Image image = new Image();
            image.setImageUrl(imageUrl);
            image.setReview(savedReview); // 연관관계 설정
            imageRepository.save(image);
        }

        return buildReviewDTO(savedReview);
    }

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

    private ReviewDTO buildReviewDTO(Review review) {
        List<String> imageUrls = review.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return new ReviewDTO(
                review.getScore(),
                review.getContent(),
                imageUrls
        );
    }
}

