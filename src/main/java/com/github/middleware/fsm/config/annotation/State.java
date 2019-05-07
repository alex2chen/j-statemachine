package com.github.middleware.fsm.config.annotation;


import java.lang.annotation.*;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface State {
    //计划支持#
    String parent() default "";

    String name();

    String alias() default "";

    String entryMethod() default "";

    String exitMethod() default "";

    boolean isInitial() default false;

    boolean isEnd() default false;
}

