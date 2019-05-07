package com.github.middleware.fsm.builder.state.support;

import com.github.middleware.fsm.builder.StateMachine;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
@Deprecated
public class SimpleStateDefine<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractState<T, S, E, C> {
    public SimpleStateDefine(S name) {
        super(name);
    }

}
