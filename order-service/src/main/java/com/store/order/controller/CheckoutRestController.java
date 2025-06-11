package com.store.api.order.controller;


import com.store.api.order.domain.CartDomain;
import com.store.common.annotation.MemberAuthorize;
import com.store.api.order.dto.CheckoutResponseDto;
import com.store.api.order.service.OrderService;
import com.store.common.annotation.CurrentUser;
import com.store.common.config.security.CustomUserDetails;
import com.store.common.http.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@MemberAuthorize
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/checkout")
public class CheckoutRestController {

    private final OrderService orderService;

    /**
     * 장바구니용 결제 정보 호출
     * @param id
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse> checkout(@CurrentUser CustomUserDetails userDetails) {
        CheckoutResponseDto checkout = orderService.getCheckoutData(userDetails.getId());
        return ResponseEntity.ok()
                .body(ApiResponse.success(checkout));
    }

    /**
     * 장바구니용 결제 정보 호출
     * @param id
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> checkout(@CurrentUser CustomUserDetails userDetails, @PathVariable Long productId, @RequestParam("quantity") int quantity) {
        CheckoutResponseDto checkout = orderService.getCheckoutData(userDetails.getId(),productId, quantity);
        return ResponseEntity.ok()
                .body(ApiResponse.success(checkout));
    }

}
