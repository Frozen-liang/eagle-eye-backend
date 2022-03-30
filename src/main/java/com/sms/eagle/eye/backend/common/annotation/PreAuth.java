package com.sms.eagle.eye.backend.common.annotation;

import com.sms.eagle.eye.backend.common.enums.PermissionType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {

    PermissionType value();
}
