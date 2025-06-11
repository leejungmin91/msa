package com.store.order.domain;

import lombok.Builder;

@Builder
public record CartDomain(Long productId, int quantity) {
}
