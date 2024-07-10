package goorm.dofarming.global.auth;

import goorm.dofarming.domain.jpa.auth.dto.response.AuthDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class DofarmingUserDetails implements UserDetails {

    private final AuthDto authDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 회원의 역할을 가져와 Spring Security 의 권한 형식으로 변환
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + authDto.role().name());

        // 역할 문자열을 SimpleGrantedAuthority 객체로 변환하여 리스트로 반환
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return authDto.password();
    }

    @Override
    public String getUsername() {
        return String.valueOf(authDto.userId());
    }

    public Long getUserId() {
        return authDto.userId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료되지 않았음을 나타내기 위해 true 반환
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠기지 않았음을 나타내기 위해 true 반환
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명이 만료되지 않았음을 나타내기 위해 true 반환
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정이 활성화되었음을 나타내기 위해 true 반환
        return true;
    }
}
