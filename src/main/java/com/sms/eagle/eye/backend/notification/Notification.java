package com.sms.eagle.eye.backend.notification;

import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Notification {

    NotificationChannelType type();
}
