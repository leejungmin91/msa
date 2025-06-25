package com.store.point.dto;

import com.store.point.entity.PointEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointDto {
    private Long userId;
    private Long point;

    public static PointDto from(PointEntity pointEntity) {
        return PointDto.builder()
                .userId(pointEntity.getUserId())
                .point(pointEntity.getPoint())
                .build();
    }
}
