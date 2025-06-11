package com.store.common.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {
    private List<String> excludeUrls;

    @Value("${prefix}")
    private String apiPrefix;

    public boolean checkExcludeUrls(String requestUrl) {
        return excludeUrls.stream()
                .map(url -> apiPrefix + url)
                .anyMatch(requestUrl::startsWith) || !requestUrl.contains(apiPrefix)
                ;
    }
}
