package com.github.middleware.fsm.builder.event.support;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.event.TransitionEndEvent;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class TransitionEndEventImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractTransitionEvent<T, S, E, C> implements TransitionEndEvent<T, S, E, C> {
    private S targetState;

    public TransitionEndEventImpl(S sourceState, S targetState, E event, C context, T stateMachine) {
        super(sourceState, event, context, stateMachine);
        this.targetState = targetState;
    }

    @Override
    public S getTargetState() {
        return targetState;
    }
}