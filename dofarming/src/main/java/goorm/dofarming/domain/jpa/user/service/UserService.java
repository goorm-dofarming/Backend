package goorm.dofarming.domain.jpa.user.service;

import goorm.dofarming.domain.jpa.user.dto.request.UserModifyRequest;
import goorm.dofarming.domain.jpa.user.dto.request.UserSignUpRequest;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.util.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    /**
     * 회원 가입
     */
    @Transactional
    public Long createUser(UserSignUpRequest userSignUpRequest) {
        isDuplicateEmail(userSignUpRequest.email());

        String nickname = "guest-" + RandomCodeGenerator.generateCode();

        /*
            이미지 생성 구현
         */
        String imageUrl = "";

        User user = User.user(userSignUpRequest.email(), nickname, userSignUpRequest.password(), imageUrl);
        User saveUser = userRepository.save(user);
        saveUser.encoder(encoder.encode(saveUser.getPassword()));

        return saveUser.getUserId();
    }

    /**
     * 회원 수정
     */
    @Transactional
    public UserResponse updateUser(UserModifyRequest userModifyRequest) {
        User user = existByUserId(userModifyRequest.userId());

        /*
            이미지 생성 구현
         */
        String imageUrl = "";

        user.updateInfo(userModifyRequest.nickname(), userModifyRequest.password(), imageUrl);
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .nickname(user.getNickname())
                .build();
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

    private void isDuplicateEmail(String email) {
        userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .ifPresent(user -> { throw new CustomException(ErrorCode.DUPLICATE_OBJECT, "Email already exists."); });
    }
}
