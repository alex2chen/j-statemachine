package com.kxtx.fsm.builder.event;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.core.utils.ReflectUtil;

import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface AfterExecActionListener<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseListener {
    String METHOD_NAME = "afterExecute";
    Method METHOD = ReflectUtil.getMethod(AfterExecActionListener.class, METHOD_NAME, new Class<?>[]{AfterExecActionEvent.class});

    void afterExecute(AfterExecActionEvent<T, S, E, C> event);
}
