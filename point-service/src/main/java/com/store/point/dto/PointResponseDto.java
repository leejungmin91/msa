package com.store.point.dto;

import com.store.point.entity.PointEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointResponseDto {
    private Long userId;
    private Long point;

    public static PointResponseDto from(PointEntity pointEntity) {
        return PointResponseDto.builder()
                .userId(pointEntity.getUserId())
                .point(pointEntity.getPoint())
                .build();
    }
}
