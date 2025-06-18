package com.store.order.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.store.order.domain.OrderPrepareDomain;
import com.store.order.domain.OrderResultDomain;
import com.store.order.entity.OrderPayDetailEntity;
import com.store.common.annotation.MemberAuthorize;
import com.store.order.domain.OrderSearchDomain;
import com.store.order.dto.*;
import com.store.order.entity.OrderEntity;
import com.store.common.annotation.CurrentUser;
import com.store.common.annotation.SkipAuthorize;
import com.store.common.util.security.CustomUserDetails;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import com.store.common.http.ApiResponse;
import com.store.order.domain.OrderCreateDomain;
import com.store.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@MemberAuthorize
@RestController
@RequiredArgsConstructor
@RequestMapping("${prefix}/order")
public class OrderRestController {

    private final OrderService orderService;

    /**
     * 주문정보 조회
     * @param userDetails
     * @return
     */
    @PostMapping("/@me")
    public ResponseEntity<ApiResponse> getMemberOrders(@CurrentUser CustomUserDetails userDetails, @RequestBody OrderSearchDomain orderSearchDomain) {
        OrderOverviewResponseDto overview = orderService.getOrderOverview(userDetails.getId(), orderSearchDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success(overview));
    }

    @GetMapping("/complete/{ordNo}")
    public ResponseEntity<ApiResponse> getOrderComplete(@CurrentUser CustomUserDetails userDetails,@PathVariable String ordNo) {
        OrderEntity entity = orderService.findByOrderNo(ordNo);
        OrderResponseDto overview = OrderResponseDto.from(entity);
        return ResponseEntity.ok()
                .body(ApiResponse.success(overview));
    }

    @GetMapping("/detail/{ordNo}")
    public ResponseEntity<ApiResponse> getOrderDetail(@CurrentUser CustomUserDetails userDetails,@PathVariable String ordNo) {
        OrderEntity entity = orderService.findByOrderNo(ordNo);
        OrderResponseDto overview = OrderResponseDto.from(entity);
        //TODO 송장번호 추가필요
        return ResponseEntity.ok()
                .body(ApiResponse.success(overview));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long id) {
        OrderEntity order = orderService.getById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        OrderResponseDto.from(order)
                ));
    }

    @PostMapping("/prepare")
    public ResponseEntity<ApiResponse> prepare(@CurrentUser CustomUserDetails user, @RequestBody OrderPrepareDomain prepare){

        OrderPrepareDomain domain = orderService.prepare(user.getId(), prepare);
        return ResponseEntity.ok()
                .body(ApiResponse.success(domain));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse> cancel(@RequestBody OrderCancelRequestDto dto, @CurrentUser CustomUserDetails user) throws Exception {
        OrderCancelResponseDto orderCancelResponseDto =  orderService.cancelOrder(dto, user);
        return ResponseEntity.ok()
                .body(ApiResponse.success(orderCancelResponseDto));
    }

}
