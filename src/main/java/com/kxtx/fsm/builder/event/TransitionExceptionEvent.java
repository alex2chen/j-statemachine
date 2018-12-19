package com.kxtx.fsm.builder.event;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.exception.StateMachineException;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface TransitionExceptionEvent<T extends StateMachine<T, S, E, C>, S, E, C> extends TransitionEvent<T, S, E, C> {
    S getTargetState();

    StateMachineException getException();
}
