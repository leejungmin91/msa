package com.store.common.util;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        String xhr = request.getHeader("X-Requested-With");

        return uri.startsWith("/api")
                || (accept != null && accept.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xhr);
    }


}
