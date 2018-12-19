package com.kxtx.fsm.builder.event;

import com.kxtx.fsm.builder.StateMachine;

/**
 * 扭转事件
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface TransitionEvent<T extends StateMachine<T, S, E, C>, S, E, C> extends StateMachineEvent<T, S, E, C> {
    S getSourceState();

    E getCause();

    C getContext();
}
