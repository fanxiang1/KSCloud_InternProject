package com.ksyun.start.camp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代表此 API 的返回对象
 */
@Data
@NoArgsConstructor
public class ApiResponse {

    /**
     * 代表此 API 的响应返回码
     * 200 表示成功，非 200 表示失败
     */
    private int code;

    private String data;

    public ApiResponse(int code, String data) {
        this.code = code;
        this.data = data;
    }
}
