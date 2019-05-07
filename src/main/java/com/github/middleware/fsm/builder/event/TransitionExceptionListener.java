package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.core.utils.ReflectUtil;

import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface TransitionExceptionListener<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseListener{
    String METHOD_NAME = "transitionException";
    Method METHOD = ReflectUtil.getMethod(TransitionExceptionListener.class, METHOD_NAME, new Class<?>[]{TransitionExceptionEvent.class});

    void transitionException(TransitionExceptionEvent<T, S, E, C> event);
}