package com.store.common.config;

import com.store.common.config.security.CustomUserDetails;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.jwt.token-validity-in-seconds}")
    private long tokenValidTime;

    @Value("${spring.security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidTime;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    private String generateToken(Authentication authentication, long totkenValidTime) {
        Claims claims = Jwts.claims().setSubject(authentication.getName()); // JWT payload 에 저장되는 정보단위
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .claim("member", userDetails)
                .claim("roles", roles)
                .setIssuedAt(new Date()) // 토큰 발행 시간 정보
                .setExpiration(new Date(new Date().getTime() + (totkenValidTime * 1000))) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, jwtSecret)  // 사용할 암호화 알고리즘
                .compact();
    }

    // JWT 토큰 생성
    public String createToken(Authentication authentication) {
        return generateToken(authentication, tokenValidTime);
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Authentication authentication) {
        String refreshToken  = generateToken(authentication, refreshTokenValidTime);
        redisService.save("refresh:user:"+authentication.getName(), refreshToken, 60 * 60 * 24 * 14); // 14일
        return refreshToken;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        List<String> roles = ((List<?>) claims.get("roles")).stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(claims.getSubject());

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        result.setDetails(userDetails);

        return result;
    }

    public String resolveToken(HttpServletRequest request, String tokenName) {
        if(request.getCookies() == null) throw new JwtException(tokenName + " 이 존재하지 않습니다.");
        // ApiException 을 상위에서 처리
        String bearerToken = Arrays.stream(request.getCookies())
                .filter(c -> tokenName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new JwtException(tokenName + " 이 존재하지 않습니다."));

        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public void validateToken(String jwtToken) throws JwtException{
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
        log.debug("토큰의 만료일자 --> {}", claims.getBody().getExpiration());
    }

    public Map<String, String> reissueToken(HttpServletRequest request) throws JwtException {

        String refreshToken = resolveToken(request, "refreshToken");
        validateToken(refreshToken);

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) getAuthentication(refreshToken);

        // Redis 에서 refreshToken 확인
        String redisRefreshToken = (String) redisService.get("refresh:user:" + token.getName());

        if (!refreshToken.equals(redisRefreshToken)) {
            throw new JwtException("Refresh Token이 일치하지 않음");
        }

        UsernamePasswordAuthenticationToken newAuthToken = new UsernamePasswordAuthenticationToken(token.getPrincipal(), null, token.getAuthorities());

        Map<String, String> tokenMap = new HashMap<>();
        // 새로운 JWT 발급
        String newToken = createToken(newAuthToken);
        SecurityContextHolder.getContext().setAuthentication(newAuthToken);

        String newRefreshToken = createRefreshToken(newAuthToken);

        tokenMap.put("token", newToken);
        tokenMap.put("refreshToken", newRefreshToken);

        return tokenMap;
    }

    public boolean isAdminFromToken(String token) {
        Claims claims = parseClaimsIgnoreExpiration(token);

        List<String> roles = ((List<?>) claims.get("roles")).stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();

        return roles.contains("ROLE_ADMIN");
    }

    private Claims parseClaimsIgnoreExpiration(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody(); // 이건 기본적으로 만료 검사 포함
        } catch (ExpiredJwtException e) {
            // 여기서도 Claims를 꺼낼 수 있음!
            return e.getClaims();
        }
    }



}
