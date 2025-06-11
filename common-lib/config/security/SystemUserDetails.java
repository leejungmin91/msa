package com.store.common.config.security;

import com.store.api.member.entity.MemberEntity;
import com.store.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemUserDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        log.debug("{} - authenticate -> 인증", this.getClass());

        MemberEntity member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found Member"));

        return new CustomUserDetails(member);
    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
