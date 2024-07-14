package goorm.dofarming.domain.jpa.auth.service;


import goorm.dofarming.domain.jpa.auth.dto.request.OauthRequest;
import goorm.dofarming.domain.jpa.auth.dto.request.SignInRequest;
import goorm.dofarming.domain.jpa.auth.dto.response.AuthDto;
import goorm.dofarming.domain.jpa.auth.dto.response.MyInfoResponse;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public String login(SignInRequest signInRequest) {

        User user = userRepository.findByEmailAndStatus(signInRequest.email(), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "User not found."));

        if (!encoder.matches(signInRequest.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH, "Password Not Match.");
        }

        AuthDto authDto = AuthDto.from(user);

        return jwtUtil.createAccessToken(authDto);
    }

    @Transactional
    public String oauthLogin(OauthRequest oauthRequest) {

        User user = User.of(oauthRequest.socialType(), oauthRequest.data());

        User findOrSaveUser = userRepository.findByEmailAndStatus(user.getEmail(), Status.ACTIVE)
                .orElseGet(() -> userRepository.save(user));

        AuthDto authDto = AuthDto.from(findOrSaveUser);

        return jwtUtil.createAccessToken(authDto);
    }

    public MyInfoResponse myInfo(DofarmingUserDetails user) {
        return MyInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getAuthDto().email())
                .nickname(user.getAuthDto().nickname())
                .imageUrl(user.getAuthDto().imageUrl())
                .role(user.getAuthDto().role())
                .build();
    }
}
