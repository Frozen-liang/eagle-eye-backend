package com.sms.eagle.eye.backend.nerko.response;

import lombok.Data;

@Data
public class NerkoBaseResponse<T> {

    private String code;
    private String message;
    private T data;
}