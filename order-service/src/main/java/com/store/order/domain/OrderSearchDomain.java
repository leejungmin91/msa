package com.store.order.domain;

import lombok.*;

import java.util.List;

@Builder
public record OrderSearchDomain(String period, String status) {
}
