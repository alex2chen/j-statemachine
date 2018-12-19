package com.kxtx.fsm.builder.event.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.TransitionExceptionEvent;
import com.kxtx.fsm.exception.StateMachineException;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class TransitionExceptionEventImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractTransitionEvent<T, S, E, C> implements TransitionExceptionEvent<T, S, E, C> {
    private S targetState;
    private StateMachineException e;

    public TransitionExceptionEventImpl(StateMachineException e,
                                        S sourceState, S targetState, E event, C context, T stateMachine) {
        super(sourceState, event, context, stateMachine);
        this.targetState = targetState;
        this.e = e;
    }

    @Override
    public S getTargetState() {
        return targetState;
    }

    @Override
    public StateMachineException getException() {
        return e;
    }
}