package com.kxtx.fsm.builder.chain;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.core.el.Dsl;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public interface From<T extends StateMachine<T, S, E, C>, S, E, C> extends Dsl {
    To<T, S, E, C> to(S state);

    To<T, S, E, C> toEnd(S state);
}
