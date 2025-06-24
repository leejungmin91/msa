package com.store.point.domain;

import lombok.Builder;

@Builder
public record PointHistoryDomain(Long userId, Long point, String description, String type) {
}
