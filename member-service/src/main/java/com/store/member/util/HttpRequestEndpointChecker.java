package com.store.member.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class HttpRequestEndpointChecker {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    // 요청 URL이 RequestMappingHandlerMapping 에 등록이 되어있는지 확인
    public boolean isEndpointExist(HttpServletRequest request) {
        try {
            if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
                ServletRequestPathUtils.parseAndCache(request);
            }
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
            if(handlerExecutionChain != null){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
