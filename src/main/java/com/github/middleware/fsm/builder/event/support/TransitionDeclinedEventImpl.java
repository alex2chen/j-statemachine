package com.github.middleware.fsm.builder.event.support;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.event.TransitionDeclinedEvent;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class TransitionDeclinedEventImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractTransitionEvent<T, S, E, C> implements TransitionDeclinedEvent<T, S, E, C> {
    public TransitionDeclinedEventImpl(S sourceState, E event, C context, T stateMachine) {
        super(sourceState, event, context, stateMachine);
    }
}
