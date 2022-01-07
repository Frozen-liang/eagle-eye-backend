package com.sms.eagle.eye.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private String message;
    private String code;
    private T data;

    @SuppressWarnings("rawtypes")
    public static ResponseBuilder ok() {
        return Response.builder().code("200").message("success");
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Response<T> ok(T data) {
        return ok().data(data).build();
    }

    @SuppressWarnings("rawtypes")
    public static Response error(String code, String message) {
        return ok().code(code).message(message).build();
    }

}