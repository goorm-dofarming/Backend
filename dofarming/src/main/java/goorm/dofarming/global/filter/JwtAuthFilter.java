package goorm.dofarming.global.filter;

import goorm.dofarming.global.auth.DofarmingUserDetailsService;
import goorm.dofarming.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final DofarmingUserDetailsService dofarmingUserDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * JWT 토큰 검증 필터 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // HTTP 요청 헤더에서 Authorization 헤더 값 가져오기
        String authorizationHeader = request.getHeader("Authorization");

        // Authorization 헤더가 존재하고 Bearer 로 시작하는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 토큰 값 추출
            String token = authorizationHeader.substring(7);

            // JWT 토큰 유효성 검사
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 회원 ID 추출
                Long userId = jwtUtil.getUserId(token);
                // 회원 ID로 UserDetails 객체 조회
                UserDetails userDetails = dofarmingUserDetailsService.loadUserByUsername(String.valueOf(userId));

                // 조회된 UserDetails 가 null 이 아닌 경우
                if (userDetails != null) {
                    // 인증 토큰 생성하여 SecurityContextHolder 에 설정
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        // 다음 필터로 요청과 응답 전달
        filterChain.doFilter(request, response);
    }
}
