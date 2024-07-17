package goorm.dofarming.global.auth;

import goorm.dofarming.domain.jpa.auth.dto.response.AuthDto;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DofarmingUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserIdAndStatus(Long.parseLong(userId), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        return new DofarmingUserDetails(AuthDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .build());
    }
}
