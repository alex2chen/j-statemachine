package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.core.utils.ReflectUtil;

import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface ExecActionExceptionListener<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseListener {
    String METHOD_NAME = "executeException";
    Method METHOD = ReflectUtil.getMethod(ExecActionExceptionListener.class, METHOD_NAME, new Class<?>[]{ExecActionExceptionEvent.class});

    void executeException(ExecActionExceptionEvent<T, S, E, C> event);
}