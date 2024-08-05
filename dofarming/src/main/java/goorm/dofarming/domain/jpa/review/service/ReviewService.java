package goorm.dofarming.domain.jpa.review.service;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.domain.jpa.image.service.ImageService;
import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.review.dto.ReviewDTO;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewCreateRequest;
import goorm.dofarming.domain.jpa.review.dto.request.ReviewModifyRequest;
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
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final ImageService imageService;

    @Transactional
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request, List<MultipartFile> files) {

        User user = userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Location location = locationRepository.findById(request.locationId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 장소입니다."));

        // Review 객체를 먼저 저장
        Review savedReview = reviewRepository.save(Review.review(user, location, request));

        // 만약 사진이 없이 빈 객체로 넘어왔으면 빈 객체도 넣지 않음.
        for (MultipartFile file : files) {
            if (Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) continue;
            String imageUrl = imageService.uploadFile(file);
            imageRepository.save(Image.image(savedReview, imageUrl));
        }

        return ReviewResponse.of(savedReview);
    }

    @Transactional
    public ReviewResponse updateReview(Long userId, List<MultipartFile> files, Long reviewId, ReviewModifyRequest request) {
        User user = userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 유저입니다."));

        Review review = reviewRepository.findReviewByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 리뷰입니다."));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "이 리뷰를 수정할 권한이 없습니다.");
        }

        review.update(request);

        for (MultipartFile file : files) {
            if (Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) continue;
            String imageUrl = imageService.uploadFile(file);
            imageRepository.save(Image.image(review, imageUrl));
        }

        return ReviewResponse.of(review);
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findReviewByReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 리뷰입니다."));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN, "이 리뷰를 삭제할 권한이 없습니다.");
        }

        review.delete();
    }

//    public ReviewDTO getReviews(Long locationId, SortType sortType) {
//        Location location = locationRepository.findById(locationId)
//                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 장소가 존재하지 않습니다."));
//
//        List<Review> reviews = reviewRepository.findByLocation_LocationIdAndStatus(locationId, Status.ACTIVE);
//
//        sortBySortType(sortType, reviews);
//
//        for (Review review : reviews) {
//            List<String> images = new ArrayList<>();
//            User user = review.getUser();
//            List<Image> imageList = imageRepository.findByReviewAndStatus(review, Status.ACTIVE);
//
//            for (Image image : imageList) {
//                images.add(image.getImageUrl());
//            }
//
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
//
//    private static void sortBySortType(SortType sortType, List<Review> reviews) {
//        switch (sortType) {
//            case HighScore:
//                reviews.sort(Comparator.comparing(Review::getScore).reversed());
//                break;
//            case LowScore:
//                reviews.sort(Comparator.comparing(Review::getScore));
//                break;
//            case HighLike:
////                reviews.sort(Comparator.comparing(Review::getLikeCount).reversed());
//                break;
//            case LowLike:
////                reviews.sort(Comparator.comparing(Review::getLikeCount));
//                break;
//            case Latest:
//                reviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
//                break;
//            case Earliest:
//                reviews.sort(Comparator.comparing(Review::getCreatedAt));
//                break;
//            default:
//                // 기본은 최신순으로
//                reviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
//                break;
//        }
//    }
}

