package com.sms.eagle.eye.backend.exception;

public class EagleEyeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    public EagleEyeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public EagleEyeException(ErrorCode errorCode, Throwable e) {
        super(errorCode.getMessage(), e);
        this.code = errorCode.getCode();
    }

    public EagleEyeException(ErrorCode errorCode, Throwable t, Object... params) {
        super(String.format(errorCode.getMessage(), params), t);
        this.code = errorCode.getCode();
    }

    public EagleEyeException(ErrorCode errorCode, Object... params) {
        super(String.format(errorCode.getMessage(), params));
        this.code = errorCode.getCode();
    }


    public String getCode() {
        return code;
    }
}