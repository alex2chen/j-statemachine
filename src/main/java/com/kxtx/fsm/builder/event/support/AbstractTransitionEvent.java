package com.kxtx.fsm.builder.event.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.TransitionEvent;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public abstract class AbstractTransitionEvent<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractStateMachineEvent<T, S, E, C> implements TransitionEvent<T, S, E, C> {
    private S sourceState;
    private E event;
    private C context;

    public AbstractTransitionEvent(S sourceState, E event, C context, T stateMachine) {
        super(stateMachine);
        this.sourceState = sourceState;
        this.event = event;
        this.context = context;
    }

    @Override
    public S getSourceState() {
        return sourceState;
    }

    @Override
    public E getCause() {
        return event;
    }

    @Override
    public C getContext() {
        return context;
    }
}
