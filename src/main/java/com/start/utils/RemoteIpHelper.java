package com.start.utils;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;

import static com.start.utils.HttpHeader.*;

@UtilityClass
public class RemoteIpHelper {

    private final String UNKNOWN = "unknown";

    public String getRemoteIpFrom(HttpServletRequest request) {
        String ip = null;
        int tryCount = 1;

        while (!isIpFound(ip) && tryCount <= 6) {
            switch (tryCount) {
                case 1:
                    ip = request.getHeader(X_FORWARDED_FOR.key());
                    break;
                case 2:
                    ip = request.getHeader(PROXY_CLIENT_IP.key());
                    break;
                case 3:
                    ip = request.getHeader(WL_PROXY_CLIENT_IP.key());
                    break;
                case 4:
                    ip = request.getHeader(HTTP_CLIENT_IP.key());
                    break;
                case 5:
                    ip = request.getHeader(HTTP_X_FORWARDED_FOR.key());
                    break;
                default:
                    ip = request.getRemoteAddr();
            }

            tryCount++;
        }

        return ip;
    }

    private boolean isIpFound(String ip) {
        return ip != null && ip.length() > 0 && !UNKNOWN.equalsIgnoreCase(ip);
    }
}