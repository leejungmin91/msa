package com.store.member.controller;

import com.store.common.http.ApiResponse;
import com.store.member.annotation.CurrentUser;
import com.store.member.config.security.CustomUserDetails;
import com.store.member.domain.SignInDomain;
import com.store.member.dto.CheckResponseDto;
import com.store.member.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.store.common.http.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthRestContoller {

    private final AuthenticationService authenticationService;
    //private final CartService cartService;

    @GetMapping("/check")
    public ResponseEntity<ApiResponse> check(@CurrentUser CustomUserDetails user){
        //List<CartItemResponseDto> cart = cartService.getCart(user.getId());
        String role = user.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        //CheckResponseDto checkResponseDto = CheckResponseDto.create(cart.size(), role);
        return ResponseEntity.ok()
                .body(ApiResponse.success(/*checkResponseDto*/));
    }

    /*@AdminAuthorize
    @GetMapping("/check-admin")
    public ResponseEntity<ApiResponse> checkAdmin(@CurrentUser CustomUserDetails user){
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }*/

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
