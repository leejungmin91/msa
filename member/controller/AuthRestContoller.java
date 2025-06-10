package com.store.api.member.controller;


import com.store.api.admin.annotation.AdminAuthorize;
import com.store.api.member.dto.CheckResponseDto;
import com.store.api.order.dto.CartItemResponseDto;
import com.store.api.order.service.CartService;
import com.store.common.annotation.CurrentUser;
import com.store.common.config.security.CustomUserDetails;
import com.store.common.http.ApiResponse;
import com.store.api.member.domain.SignInDomain;
import com.store.api.member.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthRestContoller {

    private final AuthenticationService authenticationService;
    private final CartService cartService;

    @GetMapping("/check")
    public ResponseEntity<ApiResponse> check(@CurrentUser CustomUserDetails user){
        List<CartItemResponseDto> cart = cartService.getCart(user.getId());
        String role = user.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        CheckResponseDto checkResponseDto = CheckResponseDto.create(cart.size(), role);
        return ResponseEntity.ok()
                .body(ApiResponse.success(checkResponseDto));
    }

    @AdminAuthorize
    @GetMapping("/check-admin")
    public ResponseEntity<ApiResponse> checkAdmin(@CurrentUser CustomUserDetails user){
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody SignInDomain signInDomain) {
        HttpHeaders httpHeaders = authenticationService.authenticate(signInDomain);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(ApiResponse.success());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        HttpHeaders httpHeaders = authenticationService.logout(request);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(ApiResponse.success());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse> reissue(HttpServletRequest request){
        HttpHeaders httpHeaders = authenticationService.reissueToken(request);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(ApiResponse.success());
    }

}
