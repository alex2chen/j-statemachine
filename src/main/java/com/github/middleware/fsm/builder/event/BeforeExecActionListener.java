package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.core.utils.ReflectUtil;

import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface BeforeExecActionListener<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseListener{
    public static final String METHOD_NAME = "beforeExecute";
    public static final Method METHOD = ReflectUtil.getMethod(
            BeforeExecActionListener.class, METHOD_NAME, new Class<?>[]{BeforeExecActionEvent.class});

    void beforeExecute(BeforeExecActionEvent<T, S, E, C> event);
}
