package com.store.member.config.security;

import com.store.common.config.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final AuthenticationEntryPoint entryPoint;
    private final AuthenticationConfig authenticationConfig;

    @Value("${prefix}")
    private String apiPrefix;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationConfig);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headerConfig ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                /* Jwt 토큰 사용 시 필수
                 * security는 기본적으로 session 기반으로 동작하는데 STATELESS 를 설정하지 않으면 인증정보를 session에 저장해 새로이 인증하지 않음.
                 */
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request ->
                        request
                                .antMatchers(HttpMethod.POST, apiPrefix + "/member/signup").permitAll()
                                .antMatchers(apiPrefix + "/member/**").authenticated()
                                .antMatchers(apiPrefix + "/admin/**").authenticated()
                                .antMatchers(HttpMethod.POST, apiPrefix + "/order").permitAll()
                                .antMatchers(apiPrefix + "/order/**").authenticated()
                                .anyRequest().permitAll()
                )
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }
}
