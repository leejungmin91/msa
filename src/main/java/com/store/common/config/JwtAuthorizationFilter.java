package com.store.common.config;

import com.store.common.config.security.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(0)
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Request Header 에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(request, "accessToken");

        // 2. validateToken 으로 토큰 유효성 검사
        try {
            // 토큰 유효성 검증
            jwtTokenProvider.validateToken(token);
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            // CustomAuthenticationToken 으로 반환됨.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            if (authentication != null) {
                request.setAttribute("isLoggedIn", true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new AccessDeniedException("권한이 없습니다.");
            }

        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getServletPath();
        return securityProperties.checkExcludeUrls(requestURI);
    }



}
