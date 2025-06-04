package com.store.api.member.service;

import com.store.api.member.entity.MemberEntity;
import com.store.api.member.repository.MemberRepository;
import com.store.common.config.JwtTokenProvider;
import com.store.api.member.domain.SignInDomain;
import com.store.common.config.security.CustomUserDetails;
import com.store.common.util.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public HttpHeaders authenticate(SignInDomain signInDomain) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDomain.email(), signInDomain.password());

        // security 인증 실행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 토큰 생성 (param : 인증토큰)
        String jwtToken = jwtTokenProvider.createToken(authentication);
        // refresh 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        log.debug("JWT issue ==> {}", jwtToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, setAccessCookie(jwtToken).toString());
        httpHeaders.add(HttpHeaders.SET_COOKIE, setRefreshCookie(refreshToken).toString());

        return httpHeaders;
    }

    public HttpHeaders reissueToken(HttpServletRequest request){
        Map<String, String> tokenMap = jwtTokenProvider.reissueToken(request);

        String token = tokenMap.get("token");
        String refreshToken = tokenMap.get("refreshToken");

        ResponseCookie accessCookie = setAccessCookie(token);
        ResponseCookie responseCookie = setRefreshCookie(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
        httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return httpHeaders;
    }

    public HttpHeaders logout(HttpServletRequest request){
        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            String refreshToken = jwtTokenProvider.resolveToken(request, "refreshToken");

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) jwtTokenProvider.getAuthentication(refreshToken);

            redisService.delete("refresh:user:" + token.getName());
        } finally {
            ResponseCookie accessCookie = deleteCookie("accessToken");
            ResponseCookie refreshCookie = deleteCookie("refreshToken");

            httpHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
            httpHeaders.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        return httpHeaders;
    }

    private ResponseCookie setRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(activeProfile.equals("prd"))
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                //.domain("yourdomain.net")
                .build();
    }

    private ResponseCookie setAccessCookie(String accessToken) {
        return ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(activeProfile.equals("prd"))
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                //.domain("yourdomain.net")
                .build();
    }

    private ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
