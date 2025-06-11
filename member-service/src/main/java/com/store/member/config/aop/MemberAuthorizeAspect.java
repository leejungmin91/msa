package com.store.member.config.aop;

import com.store.common.annotation.SkipAuthorize;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class MemberAuthorizeAspect {

    @Around("@annotation(com.store.common.annotation.MemberAuthorize) || @within(com.store.common.annotation.MemberAuthorize)")
    public Object checkMemberRole(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 1. 메서드에 @SkipAuthorize 있으면 권한 체크 건너뜀
        if (method.isAnnotationPresent(SkipAuthorize.class)) {
            return joinPoint.proceed();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(ApiCode.PENDING_MEMBER);
        }

        boolean hasPendingRole = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PENDING"));

        if (hasPendingRole) {
            throw new ApiException(ApiCode.PENDING_MEMBER);
        }

        return joinPoint.proceed();
    }
}

