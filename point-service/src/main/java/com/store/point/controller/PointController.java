package com.store.point.controller;

import com.store.common.http.ApiResponse;
import com.store.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/point")
@RestController
public class PointController {

    private final PointService pointService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getPoint(@PathVariable Long userId) {
        return ResponseEntity.ok(
                ApiResponse.success(pointService.getUserPoint(userId))
        );
    }

}
