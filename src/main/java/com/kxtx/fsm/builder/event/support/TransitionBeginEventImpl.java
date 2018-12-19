package com.kxtx.fsm.builder.event.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.TransitionBeginEvent;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class TransitionBeginEventImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractTransitionEvent<T, S, E, C> implements TransitionBeginEvent<T, S, E, C> {
    public TransitionBeginEventImpl(S sourceState, E event, C context, T stateMachine) {
        super(sourceState, event, context, stateMachine);
    }
}
