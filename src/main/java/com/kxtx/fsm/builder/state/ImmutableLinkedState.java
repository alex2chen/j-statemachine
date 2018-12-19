package com.kxtx.fsm.builder.state;

import com.kxtx.fsm.builder.StateMachine;

/**
 * Created by YT on 2017/3/8.
 */
public interface ImmutableLinkedState<T extends StateMachine<T, S, E, C>, S, E, C> extends StateDefine<T, S, E, C> {

    /**
     * @return linked state machine
     */
    StateMachine<? extends StateMachine<?, S, E, C>, S, E, C> getLinkedStateMachine(T stateMachine);
}
