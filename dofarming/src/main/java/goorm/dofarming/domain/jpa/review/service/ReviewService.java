package goorm.dofarming.domain.jpa.review.service;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.domain.jpa.image.service.ImageService;
import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.review.dto.ReviewDTO;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.review.dto.ReviewResponse;
import goorm.dofarming.domain.jpa.review.repository.ReviewRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final ImageService imageService;

    @Transactional
    public ReviewResponse createReview(List<MultipartFile> files, Long userId, Long locationId, Double score, String content) {

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

        // 만약 사진이 없이 빈 객체로 넘어왔으면 빈 객체도 넣지 않음.
        for (MultipartFile file : files) {
            if (Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) continue;
            String imageUrl = imageService.uploadFile(file);
            images.add(imageUrl); // 반환할 이미지 리스트 저장
            Image image = new Image();
            image.setImageUrl(imageUrl);
            image.setReview(savedReview); // 연관관계 설정
            imageRepository.save(image);
        }

        return buildReviewResponse(user, savedReview, images);
    }

    @Transactional
    public ReviewResponse updateReview(List<MultipartFile> files, Long reviewId, Double score, String content) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 리뷰입니다."));

        review.setContent(content);
        review.setScore(score);

        imageRepository.deleteAll(review.getImages());

        review.getImages().clear();
        Review savedReview = reviewRepository.save(review);

        List<String> updateImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) continue;
            String imageUrl = imageService.uploadFile(file);
            updateImages.add(imageUrl); // 반환할 이미지 리스트 저장
            Image image = new Image();
            image.setImageUrl(imageUrl);
            image.setReview(savedReview); // 연관관계 설정
            imageRepository.save(image);
        }

        reviewRepository.save(review);

        return buildReviewResponse(review.getUser(), review, updateImages);
    }

    public ReviewDTO getReviews(Long locationId, SortType sortType) {
        List<ReviewResponse> reviewResponses = new ArrayList<>();

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 장소가 존재하지 않습니다."));

        List<Review> reviews = location.getReviews();

        Double averageScore = calAverageScore(location);

        sortBySortType(sortType, reviews);


        for (Review review : reviews) {
            List<String> images = new ArrayList<>();
            User user = review.getUser();
            for (Image image : review.getImages()) {
                images.add(image.getImageUrl());
            }
            ReviewResponse reviewDTO = buildReviewResponse(user, review, images);
            reviewResponses.add(reviewDTO);
        }

        boolean liked = likeRepository.existsByLocation_LocationIdAndStatus(locationId, Status.ACTIVE);

        // 대표이미지가 없고, 리뷰가 있는 경우
        if (location.getImage().isEmpty() && !reviews.isEmpty()) {
            int order = reviews.size() - 1;
            for (int i = order; i >= 0; i--) {
                Review review = reviews.get(i);
                if (!review.getImages().isEmpty()) {
                    location.setImage(review.getImages().get(0).getImageUrl());
                    break;
                }
            }
        }

        return ReviewDTO.of(location, reviewResponses, liked, averageScore);
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

    // 평균점수
    public Double calAverageScore(Location location) {
        Double averScore = 0.0;
        if (location.getReviews().isEmpty()) return averScore;

        for (Review review : location.getReviews()) {
            averScore += review.getScore();
        }
        return averScore / location.getReviews().size();
    }

    private ReviewResponse buildReviewResponse(User user, Review review, List<String> imageUrls) {

        Double userAverageScore = 0.0;
        for (Review userReview : user.getReviews()) {
            userAverageScore += userReview.getScore();
        }
        userAverageScore /= user.getReviews().size();

        return new ReviewResponse(
                review.getReviewId(),
                review.getScore(),
                review.getContent(),
                user.getImageUrl(),
                user.getNickname(),
                user.getReviews().size(),
                userAverageScore,
                imageUrls
        );
    }

    private static void sortBySortType(SortType sortType, List<Review> reviews) {
        switch (sortType) {
            case HighScore:
                reviews.sort(Comparator.comparing(Review::getScore).reversed());
                break;
            case LowScore:
                reviews.sort(Comparator.comparing(Review::getScore));
                break;
            case HighLike:
//                reviews.sort(Comparator.comparing(Review::getLikeCount).reversed());
                break;
            case LowLike:
//                reviews.sort(Comparator.comparing(Review::getLikeCount));
                break;
            case Latest:
                reviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
                break;
            case Earliest:
                reviews.sort(Comparator.comparing(Review::getCreatedAt));
                break;
            default:
                // 기본은 최신순으로
                reviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
                break;
        }
    }

    // 리뷰만 반환하던 버전
//    public List<ReviewResponse> getReviews(Long locationId) {
//        List<ReviewResponse> result = new ArrayList<>();
//
//        Location location = locationRepository.findById(locationId)
//                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 장소가 존재하지 않습니다."));
//
//        List<Review> reviews = location.getReviews();
//
//        Double averageScore = calAverageScore(location);
//
//        for (Review review : reviews) {
//            List<String> images = new ArrayList<>();
//            User user = review.getUser();
//            for (Image image : review.getImages()) {
//                images.add(image.getImageUrl());
//            }
//            ReviewResponse reviewDTO = buildReviewResponse(user, review, images, averageScore);
//            result.add(reviewDTO);
//        }
//        return result;
//    }

    // reviewDTO 초기버전
//    public ReviewDTO getReviews(Long locationId) {
//        List<ReviewResponse> reviewResponses = new ArrayList<>();
//
//        Location location = locationRepository.findById(locationId)
//                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 장소가 존재하지 않습니다."));
//
//        List<Review> reviews = location.getReviews();
//
//        Double averageScore = calAverageScore(location);
//
//        for (Review review : reviews) {
//            List<String> images = new ArrayList<>();
//            User user = review.getUser();
//            for (Image image : review.getImages()) {
//                images.add(image.getImageUrl());
//            }
//            ReviewResponse reviewDTO = buildReviewResponse(user, review, images);
//            reviewResponses.add(reviewDTO);
//        }
//
//        boolean liked = likeRepository.existsByLocation_LocationIdAndStatus(locationId, Status.ACTIVE);
//
//        // 대표이미지가 없고, 리뷰가 있는 경우
//        if (location.getImage().isEmpty() && !reviews.isEmpty()) {
//            int order = reviews.size() - 1;
//            for (int i = order; i >= 0; i--) {
//                Review review = reviews.get(i);
//                if (!review.getImages().isEmpty()) {
//                    location.setImage(review.getImages().get(0).getImageUrl());
//                    break;
//                }
//            }
//        }
//
//        return ReviewDTO.of(location, reviewResponses, liked, averageScore);
//    }
}

