package com.store.common.config.security;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AuthenticationConfig implements AuthenticationProvider {

    private final SystemUserDetails systemUserDetails;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String id = (String) token.getPrincipal();

        CustomUserDetails customUserDetails = (CustomUserDetails) systemUserDetails.loadUserByUsername(id);

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), customUserDetails.getPassword())){
            throw new BadCredentialsException("이메일 또는 비밀번호를 확인해주세요.");
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        result.setDetails(customUserDetails);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
