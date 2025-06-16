package com.store.member.controller;


import com.store.member.annotation.CurrentUser;
import com.store.member.annotation.MemberAuthorize;
import com.store.member.config.security.CustomUserDetails;
import com.store.member.domain.ShippingCreateDomain;
import com.store.member.domain.ShippingUpdateDomain;
import com.store.member.dto.ShippingResponseDto;
import com.store.member.entity.ShippingEntity;
import com.store.member.service.ShippingService;
import com.store.common.http.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@MemberAuthorize
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/shipping")
public class ShippingRestController {

    private final ShippingService shippingService;

    @GetMapping("/@me")
    public ResponseEntity<ApiResponse> getMyShippingList(@CurrentUser CustomUserDetails userDetails) {
        List<ShippingEntity> shippingList = shippingService.getShippingList(userDetails.getId());
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                                shippingList.stream()
                                        .map(ShippingResponseDto::from)
                                        .toList()
                        )
                );
    }

    @PostMapping("/@me")
    public ResponseEntity<ApiResponse> saveMyShipping(@CurrentUser CustomUserDetails userDetails, @RequestBody ShippingCreateDomain shippingCreateDomain) {
        shippingService.saveShipping(userDetails.getId(), shippingCreateDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }

    @GetMapping("/@me/{id}")
    public ResponseEntity<ApiResponse> getMyShipping(@CurrentUser CustomUserDetails userDetails, @PathVariable Long id) {
        ShippingEntity shipping = shippingService.getMyShipping(id, userDetails.getId());
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        ShippingResponseDto.from(shipping)
                ));
    }

    @PutMapping("/@me/{id}")
    public ResponseEntity<ApiResponse> updateMyShipping(@PathVariable Long id, @RequestBody ShippingUpdateDomain shippingUpdateDomain) {
        ShippingEntity shipping = shippingService.updateShipping(id, shippingUpdateDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        ShippingResponseDto.from(shipping)
                ));
    }

    @DeleteMapping("/@me/{id}")
    public ResponseEntity<ApiResponse> deleteMyShipping(@PathVariable Long id) {
        shippingService.deleteShipping(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }


}
