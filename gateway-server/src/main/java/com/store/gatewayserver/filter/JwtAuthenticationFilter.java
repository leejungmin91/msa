package com.store.gatewayserver.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    private static final List<String> WHITELIST_PATHS = List.of(
            "/product/**",
            "/product-service/**",
            "/actuator/**"
    );

    private JwtParser jwtParser;

    @PostConstruct
    void init() {
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String path = exchange.getRequest().getURI().getPath();

        boolean whiteByPath  = WHITELIST_PATHS.stream().anyMatch(p -> new AntPathMatcher().match(p, path));
        if (whiteByPath) {
            return chain.filter(exchange); // 인증 건너뛰기
        }

        // 1. Authorization 헤더에서 Bearer 토큰 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            log.info("authHeader is null or not start with Bearer");
            return exchange.getResponse().setComplete();
        }
        String token = authHeader.substring(7);

        try {
            // 2. JWT 검증
            Claims claims = jwtParser.parseClaimsJws(token)
                    .getBody();

            // 3. 유저 정보(예: userId, roles 등) 헤더로 downstream에 추가
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", claims.getSubject()) // 예시로 sub(userId) 추가
                    // .header("X-User-Role", claims.get("role", String.class))
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return chain.filter(mutatedExchange);
        } catch (JwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    // 필터 우선순위 (값이 낮을수록 먼저 동작)
    @Override
    public int getOrder() {
        return -1;
    }
}
