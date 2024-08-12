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
import goorm.dofarming.domain.jpa.review_like.repository.ReviewLikeRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ImageService imageService;

    @Transactional
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request, List<MultipartFile> files) {

        User user = userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Location location = locationRepository.findById(request.locationId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 장소입니다."));

        // Review 객체를 먼저 저장
        Review savedReview = reviewRepository.save(Review.review(user, location, request));

        boolean liked = reviewLikeRepository.existsByReview_ReviewIdAndUser_UserIdAndStatus(savedReview.getReviewId(), user.getUserId(), Status.ACTIVE);
        // 만약 사진이 없이 빈 객체로 넘어왔으면 빈 객체도 넣지 않음.
        if (files == null || files.isEmpty()) return ReviewResponse.of(liked, savedReview);

        for (MultipartFile file : files) {
            String imageUrl = imageService.uploadFile(file);
            imageRepository.save(Image.image(savedReview, imageUrl));
        }

        return ReviewResponse.of(liked, savedReview);
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

        boolean liked = reviewLikeRepository.existsByReview_ReviewIdAndUser_UserIdAndStatus(review.getReviewId(), user.getUserId(), Status.ACTIVE);

        if (files == null || files.isEmpty()) return ReviewResponse.of(liked, review);

        for (MultipartFile file : files) {
            String imageUrl = imageService.uploadFile(file);
            imageRepository.save(Image.image(review, imageUrl));
        }

        return ReviewResponse.of(liked, review);
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

    public List<ReviewResponse> getReviews(Long userId, Integer reviewLikeCount, Double score, Long locationId, Long reviewId, LocalDateTime createdAt, SortType sortType) {
        User user = userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 유저입니다."));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 장소가 존재하지 않습니다."));

        return reviewRepository.search(user.getUserId(), reviewLikeCount, score, location.getLocationId(), reviewId, createdAt, sortType)
                .stream().map(review -> {
                    boolean liked = reviewLikeRepository.existsByReview_ReviewIdAndUser_UserIdAndStatus(review.getReviewId(), user.getUserId(), Status.ACTIVE);
                    return ReviewResponse.of(liked, review);
                }).collect(Collectors.toList());
    }

    public ReviewResponse getMyReview(Long userId, Long locationId) {
        User user = userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 유저입니다."));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 장소가 존재하지 않습니다."));

        Review review = reviewRepository.findByLocation_LocationIdAndUser_UserIdAndStatus(location.getLocationId(), user.getUserId(), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 유저의 리뷰가 존재하지 않습니다."));

        boolean liked = reviewLikeRepository.existsByReview_ReviewIdAndUser_UserIdAndStatus(review.getReviewId(), user.getUserId(), Status.ACTIVE);

        return ReviewResponse.of(liked, review);
    }
}

