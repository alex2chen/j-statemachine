package com.kxtx.fsm.builder.event.annotation;

import java.lang.annotation.*;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AsyncExec {
    long timeout() default -1;
}