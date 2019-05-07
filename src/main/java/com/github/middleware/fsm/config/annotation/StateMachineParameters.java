package com.github.middleware.fsm.config.annotation;

import java.lang.annotation.*;

/**
 * 状态机的参数（针对ProxyStateMachine类型纠正）
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StateMachineParameters {
    Class<?> stateType();

    Class<?> eventType();

    Class<?> contextType();
}
