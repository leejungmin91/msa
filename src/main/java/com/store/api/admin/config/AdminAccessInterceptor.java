package com.store.api.admin.config;

import com.store.common.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AdminAccessInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = jwtTokenProvider.resolveToken(request, "accessToken");
        if(!jwtTokenProvider.isAdminFromToken(accessToken)) {
            response.sendRedirect("/403");
            return false;
        }
        return true;
    }
}
