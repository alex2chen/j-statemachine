package com.kxtx.fsm.filter.annotation;

import java.lang.annotation.*;

/**
 * 状态机实例化后预处理标示
 * Created by YT on 2017/3/12.
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostConstruct {
}
