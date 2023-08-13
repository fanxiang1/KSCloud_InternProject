package com.ksyun.trade.utils;

import java.util.UUID;

public class RequestIdUtils {
    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();
    private RequestIdUtils() {
    }
    public static void generateRequestId() {
        requestIdHolder.set(UUID.randomUUID().toString());
    }
    public static void generateRequestId(String  requestId) {
        requestIdHolder.set(requestId);
    }
    public static String getRequestId() {
        return requestIdHolder.get();
    }
    public static void removeRequestId() {
        requestIdHolder.remove();
    }
}
