package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.StateMachine;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface TransitionEndEvent<T extends StateMachine<T, S, E, C>, S, E, C> extends TransitionEvent<T, S, E, C> {
    S getTargetState();
}
