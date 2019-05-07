package com.github.middleware.fsm.config.annotation;

import com.github.middleware.fsm.filter.validator.PreChecker;
import com.github.middleware.fsm.filter.validator.support.PreCheckers;

import java.lang.annotation.*;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Transition {
    //支持*
    String from();
    //支持*
    String to();
    //支持*
    String on();

    //when prechecker
    Class<? extends PreChecker> whenPreChecker() default PreCheckers.Always.class;
    //when mvel 与上面二选一
    String whenMVEL() default "";

    boolean isTargetEnd() default false;

    String complete() default "";
}

