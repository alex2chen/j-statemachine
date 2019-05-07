package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.exec.Action;

/**
 * action事件
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface ActionEvent<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseEvent {
    Action<T, S, E, C> getExecutionTarget();

    S getFrom();

    S getTo();

    E getEvent();

    C getContext();

    T getStateMachine();

    int[] getMOfN();
}