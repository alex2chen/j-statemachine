package com.kxtx.fsm.builder.state.support;

import com.kxtx.fsm.builder.StateMachine;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
@Deprecated
public class ChoiceStateDefine<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractState<T, S, E, C> {
    public ChoiceStateDefine(S name) {
        super(name);
    }
}
