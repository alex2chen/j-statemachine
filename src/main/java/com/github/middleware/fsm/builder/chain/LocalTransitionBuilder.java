package com.github.middleware.fsm.builder.chain;

import com.github.middleware.fsm.builder.StateMachine;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public interface LocalTransitionBuilder<T extends StateMachine<T, S, E, C>, S, E, C> {
    From<T, S, E, C> from(S state);
}
