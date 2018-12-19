package com.kxtx.fsm.builder.exec;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.BeforeExecActionListener;
import com.kxtx.fsm.builder.exec.dto.ExecServiceContext;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface ActionExecService<T extends StateMachine<T, S, E, C>, S, E, C> {
    void execute(ExecServiceContext execServiceContext);

    void addProxyListener(Class<?> eventType, Object listener, Method method);

    void addExecActionListener(BeforeExecActionListener<T, S, E, C> listener);

    void removeExecActionListener(BeforeExecActionListener<T, S, E, C> listener);
}
