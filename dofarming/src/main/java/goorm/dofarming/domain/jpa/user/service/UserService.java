package goorm.dofarming.domain.jpa.user.service;

import goorm.dofarming.domain.jpa.user.dto.request.UserModifyRequest;
import goorm.dofarming.domain.jpa.user.dto.request.UserSignUpRequest;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.domain.jpa.user.entity.Role;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.util.ImageUtil;
import goorm.dofarming.global.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ImageUtil imageUtil;

    /**
     * 회원 가입
     */
    @Transactional
    public Long createUser(UserSignUpRequest userSignUpRequest) {
        isDuplicateEmail(userSignUpRequest.email(), Role.DOFARMING);

        String nickname = RandomGenerator.generateNickname();

        User user = User.user(userSignUpRequest.email(), nickname, userSignUpRequest.password());
        User saveUser = userRepository.save(user);
        saveUser.encoder(encoder.encode(saveUser.getPassword()));

        return saveUser.getUserId();
    }

    /**
     * 회원 수정
     */
    @Transactional
    public UserResponse updateUser(Long userId, MultipartFile file, UserModifyRequest userModifyRequest) {
        User user = existByUserId(userId);

        /*
         * 패스워드 처리
         */
        String userPassword = user.getPassword();
        if (userModifyRequest.password() != null && !encoder.matches(userModifyRequest.password(), userPassword)) {
            userPassword = encoder.encode(userModifyRequest.password());
        }

        /*
         * 이미지 생성
         */
        if (file == null) {
            user.updateInfo(userModifyRequest.nickname(), userPassword, user.getImageUrl());

        } else if (file.isEmpty()) {
            // 이미지가 존재하면 삭제
            deleteUserImageFIle(user);
            // 유저 업데이트
            user.updateInfo(userModifyRequest.nickname(), userPassword, null);

        } else {
            String imageUrl = imageUtil.getImageUrl(file);

            Path path = imageUtil.makeFilePath(imageUrl);

            // 이미지가 존재하면 삭제
            deleteUserImageFIle(user);
            // 경로에 이미지 쓰기
            imageUtil.writeImageFile(path, file);

            user.updateInfo(userModifyRequest.nickname(), userPassword, imageUrl);
        }

        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .nickname(user.getNickname())
                .build();
    }

    private void deleteUserImageFIle(User user) {
        if (user.getImageUrl() != null) {
            // 유저의 이미지 삭제
            Path existImageFilePath = Paths.get(user.getImageUrl());
            imageUtil.deleteImageUrl(existImageFilePath);
        }
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void userDelete(Long userId) {
        User user = existByUserId(userId);
        user.delete();
    }

    /**
     * 회원 조회
     */
    public UserResponse getUser(Long userId) {
        User user = existByUserId(userId);
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .nickname(user.getNickname())
                .build();
    }

    private User existByUserId(Long userId) {
        return userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "User not found."));
    }

    private void isDuplicateEmail(String email, Role role) {
        userRepository.findByEmailAndRoleAndStatus(email, role, Status.ACTIVE)
                .ifPresent(user -> { throw new CustomException(ErrorCode.DUPLICATE_OBJECT, "Email already exists."); });
    }
}
