package com.github.middleware.fsm.builder.event.support;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.event.StateMachineEvent;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public abstract class AbstractStateMachineEvent<T extends StateMachine<T, S, E, C>, S, E, C> implements StateMachineEvent<T, S, E, C> {
    private T stateMachine;

    public AbstractStateMachineEvent(T stateMachine) {
        this.stateMachine = stateMachine;
    }
    @Override
    public T getStateMachine() {
        return stateMachine;
    }
}
