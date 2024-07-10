package goorm.dofarming.global.util;

import goorm.dofarming.domain.jpa.auth.dto.response.AuthDto;
import goorm.dofarming.global.common.error.exception.CustomExpiredJwtException;
import goorm.dofarming.global.common.error.exception.CustomInvalidJwtException;
import goorm.dofarming.global.common.error.exception.CustomMalformedJwtException;
import goorm.dofarming.global.common.error.exception.CustomUnsupportedJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ) {
        // Base64로 인코딩된 비밀 키를 디코딩하여 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // HMAC SHA 알고리즘을 사용하여 JWT 서명에 사용할 키를 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
        // JWT 액세스 토큰의 만료 시간을 설정
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     * @param authDto
     * @return Access Token String
     */
    public String createAccessToken(AuthDto authDto) {
        return createToken(authDto, accessTokenExpTime);
    }

    /**
     * JWT 생성
     * @param authDto
     * @param expireTime
     * @return JWT String
     */
    private String createToken(AuthDto authDto, long expireTime) {
        /*
            클레임(Claims)은 토큰 내에 포함된 정보를 나타낸다.
            토큰의 페이로드 부분에 위치하며, 발급한 주체에 관한 정보를 포함할 수 있다.
            클레임은 세 가지 범주로 나뉜다.
            1. 등록된 클레임 : JWT 표준에 의해 정의된 클레임
            2. 공개 클레임 : 사용자 정의 클레임으로 공개적으로 사용되는 정보 포함 (URI 형식으로 네임스페이스를 지정)
            3. 비공개 클레임 : 발급자와 수신자 간에 합의된 사용자 정의 클레임 (네임스페이스 X)
        */
        // JWT 클레임 생성
        Claims claims = Jwts.claims();
        claims.put("userId", authDto.userId());
        claims.put("email", authDto.email());
        claims.put("role", authDto.role());

        // 현재 시간
        ZonedDateTime now = ZonedDateTime.now();
        // 토큰 만료 시간 설정
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        // JWT 생성
        return Jwts.builder()
                .setClaims(claims) // 클레임 설정
                .setIssuedAt(Date.from(now.toInstant())) // 토큰 발행 시간 설정
                .setExpiration(Date.from(tokenValidity.toInstant())) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 및 비밀 키 설정
                .compact(); // JWT 문자열로 직렬화
    }

    /**
     * Token 에서 Member ID 추출
     * @param token
     * @return Member ID
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    /** JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            // JWT 파서 빌더를 사용하여 JWT 파서 생성
            return Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 검증을 위한 비밀 키 설정
                    .build() // 파서 빌드
                    .parseClaimsJws(accessToken) // JWT 액세스 토큰을 파싱하여 클레임 추출
                    .getBody(); // 클레임 객체 반환
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우 ExpiredJwtException 예외가 발생
            return e.getClaims();
        }
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // JWT 서명 오류 또는 잘못된 형식의 JWT
            throw new CustomInvalidJwtException("유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            // JWT 토큰이 만료된 경우
            throw new CustomExpiredJwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 토큰
            throw new CustomUnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            // JWT 클레임 문자열이 비어 있는 경우
            throw new CustomMalformedJwtException("잘못된 형식의 JWT 토큰입니다.");
        } catch (Exception e) {
            return false;
        }
    }
}
