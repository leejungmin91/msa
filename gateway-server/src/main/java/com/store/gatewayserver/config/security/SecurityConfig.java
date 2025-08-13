package com.store.gatewayserver.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(ex -> ex
                        // 게이트웨이에서 보통 열어두는 엔드포인트
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/member-service/**", "/point-service/**", "/notification-service/**").permitAll()
                        // 그 외 모두 허용 (원하시면 여기만 authenticated()로 바꾸고 JWT 붙이세요)
                        .anyExchange().permitAll()
                )
                .build();
    }
}
