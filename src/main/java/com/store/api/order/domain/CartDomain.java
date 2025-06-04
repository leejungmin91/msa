package com.store.api.order.domain;

import lombok.Builder;

@Builder
public record CartDomain(Long productId, int quantity) {
}
