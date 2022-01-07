package com.sms.eagle.eye.backend.exception;

import com.sms.eagle.eye.backend.model.Response;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EagleEyeException.class)
    public Response<Object> customExceptionHandler(HttpServletRequest request, final EagleEyeException e,
        HttpServletResponse response) {
        log.error("[Exception Level=BUSINESS_ERROR]:", e);
        return Response.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Response<Object> unauthorizedExceptionHandler(final Exception e,
        HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return Response.error(Integer.toString(HttpStatus.UNAUTHORIZED.value()),
            HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @ExceptionHandler(RuntimeException.class)
    public Response<Object> runtimeExceptionHandler(final Exception e,
        HttpServletResponse response) {
        log.error("[Exception Level=INTERNAL_SERVER_ERROR]:", e);
        return Response.error(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            return new ResponseEntity<>(Response.error(Integer.toString(status.value()),
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()), status);
        }
        if (ex instanceof BindException) {
            BindException exception = (BindException) ex;
            return new ResponseEntity<>(Response.error(Integer.toString(status.value()),
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()), status);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            log.error("[Exception Level=METHOD_ARGUMENT_ERROR]:", ex);
            return new ResponseEntity<>(
                Response.error(Integer.toString(status.value()), "Failed to convert argument"), status);
        }
        log.error("[Exception Level=INTERNAL_SERVER_ERROR]:", ex);
        return new ResponseEntity<>(Response.error(Integer.toString(status.value()), "Server error"), status);
    }

}