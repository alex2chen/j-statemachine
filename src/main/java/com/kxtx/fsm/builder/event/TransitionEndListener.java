package com.kxtx.fsm.builder.event;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.core.utils.ReflectUtil;

import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface TransitionEndListener<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseListener{
    String METHOD_NAME = "transitionEnd";
    Method METHOD = ReflectUtil.getMethod(TransitionEndListener.class, METHOD_NAME, new Class<?>[]{TransitionEndEvent.class});

    void transitionEnd(TransitionEndEvent<T, S, E, C> event);
}
