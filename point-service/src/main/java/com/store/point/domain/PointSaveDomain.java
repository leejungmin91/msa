package com.store.point.domain;

import lombok.Builder;

@Builder
public record PointSaveDomain(Long userId, Long point, String description, String type) {
}
