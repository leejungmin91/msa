package com.store.api.order.domain;

import lombok.*;

import java.util.List;

@Builder
public record OrderSearchDomain(String period, String status) {
}
