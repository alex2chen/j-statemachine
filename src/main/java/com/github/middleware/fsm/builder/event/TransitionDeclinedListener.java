package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.core.utils.ReflectUtil;

import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface TransitionDeclinedListener<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseListener {
    String METHOD_NAME = "transitionDeclined";
    Method METHOD = ReflectUtil.getMethod(TransitionDeclinedListener.class, METHOD_NAME, new Class<?>[]{TransitionDeclinedEvent.class});

    void transitionDeclined(TransitionDeclinedEvent<T, S, E, C> event);
}