package com.store.api.order.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kisa")
@Getter
@Setter
public class KisaProperties {
    private String mid;
    private String merchantKey;
    private String payReturnUrl;
    private String cancelReturnUrl;
    private String apiUrl;
    private String currencyType;
    private String model;
    // lombok @Getter/@Setter 또는 직접 게터/세터 작성
}

