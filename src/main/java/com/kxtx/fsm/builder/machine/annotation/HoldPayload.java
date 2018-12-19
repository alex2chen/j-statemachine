package com.kxtx.fsm.builder.machine.annotation;

import java.lang.annotation.*;

/**
 * 是否携带Payload
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HoldPayload {
}
