package com.store.common.config;
import com.store.member.util.HttpRequestEndpointChecker;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    @Autowired
    private HttpRequestEndpointChecker httpRequestEndpointChecker;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (authException == null) {
            authException = new AuthenticationException("Unauthorized access") {};
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
            return;
        }

        if (!httpRequestEndpointChecker.isEndpointExist(request)) {
            HttpHeaders httpHeaders = Collections.list(request.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            h -> Collections.list(request.getHeaders(h)),
                            (oldValue, newValue) -> newValue,
                            HttpHeaders::new
                    ));
            resolver.resolveException(request, response, null, new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), httpHeaders));
        } else {
            Exception exception = (Exception) request.getAttribute("exception");

            // 예외가 `null`인 경우 기본 예외를 생성하여 처리
            if (exception == null) {
                exception = new ApiException(ApiCode.ACCESS_DENIED) {}; // 기본 예외 설정
            }

            resolver.resolveException(request, response, null, exception);
        }
    }

}
