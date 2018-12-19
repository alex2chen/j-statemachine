package com.kxtx.fsm.builder.plugin.log;

import java.lang.annotation.*;

/**
 * 记录执行时间
 * Debug模式
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogExec {
    //when mvel
    String when() default "1==1";
}
