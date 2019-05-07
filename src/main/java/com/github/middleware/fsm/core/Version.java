package com.github.middleware.fsm.core;

import java.lang.annotation.*;

/**
 * 定位：状态扭转的版本化
 * TODO：再定
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/1
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Version {
    String version() default "";
}
