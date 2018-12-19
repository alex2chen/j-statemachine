package com.kxtx.fsm.builder.state;


import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.core.el.Dsl;
import com.kxtx.fsm.builder.chain.TransitionDefine;
import com.kxtx.fsm.builder.state.support.AbstractState;

/**
 * State链式语法支持
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */

public interface StateCriteria<T extends StateMachine<T, S, E, C>, S, E, C> extends Dsl {
    AbstractState<T, S, E, C> choice(S name);

    TransitionDefine<T, S, E, C> from(S S);

    TransitionDefine<T, S, E, C> fromChoice(S choiceName);
}
