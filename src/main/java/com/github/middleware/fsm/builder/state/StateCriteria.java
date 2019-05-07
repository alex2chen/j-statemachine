package com.github.middleware.fsm.builder.state;


import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.core.el.Dsl;
import com.github.middleware.fsm.builder.chain.TransitionDefine;
import com.github.middleware.fsm.builder.state.support.AbstractState;

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
