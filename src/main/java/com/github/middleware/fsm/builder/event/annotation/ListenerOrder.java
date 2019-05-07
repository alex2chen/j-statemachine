package com.github.middleware.fsm.builder.event.annotation;

import java.lang.annotation.*;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ListenerOrder {
    int value();
}
