package com.start.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbDateFormat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse implements Serializable {

    private static final long serialVersionUID = 42L;

    private final long timestamp = System.currentTimeMillis();
    @JsonbDateFormat("DD-MM-YYY HH:mm:ss")
    private final ZonedDateTime dateTime = ZonedDateTime.now();
    private Map<String, Object> data = new HashMap<>();
    private int status;
    private String message;

    public static ApiResponse ok() {
        return ApiResponse
                .builder()
                .status(200)
                .message(OK.getReasonPhrase())
                .build();
    }

    public static ApiResponse ok(Map<String, Object> data) {
        return ApiResponse
                .builder()
                .status(200)
                .data(data)
                .message(OK.getReasonPhrase())
                .build();
    }

    public static ApiResponse bad() {
        return ApiResponse
                .builder()
                .status(400)
                .message(BAD_REQUEST.getReasonPhrase())
                .build();
    }

    public static ApiResponse bad(Map<String, Object> data) {
        return ApiResponse
                .builder()
                .status(400)
                .data(data)
                .message(BAD_REQUEST.getReasonPhrase())
                .build();
    }

    public static ApiResponse accessDenied() {
        return ApiResponse
                .builder()
                .status(403)
                .message(FORBIDDEN.getReasonPhrase())
                .build();
    }

    public static ApiResponse accessDenied(Map<String, Object> data) {
        return ApiResponse
                .builder()
                .status(403)
                .data(data)
                .message(FORBIDDEN.getReasonPhrase())
                .build();
    }

    public static ApiResponse error() {
        return ApiResponse
                .builder()
                .status(500)
                .message(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
    }

}
