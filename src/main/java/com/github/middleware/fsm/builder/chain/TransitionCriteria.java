package com.github.middleware.fsm.builder.chain;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.core.el.Dsl;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.filter.validator.PreChecker;

/**
 * 链式语法支持
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
@Deprecated
public interface TransitionCriteria<T extends StateMachine<T, S, E, C>, S, E, C> extends Dsl {
    @Deprecated
    TransitionDefine<T, S, E, C> to(S stateName);
    @Deprecated
    TransitionDefine<T, S, E, C> toChoice(S choiceName);
    @Deprecated
    TransitionDefine<T, S, E, C> onEvent(E event);
    @Deprecated
    TransitionDefine<T, S, E, C> preCheckerElse();
    @Deprecated
    TransitionDefine<T, S, E, C> preChecker(PreChecker<C> preChecker);
    @Deprecated
    TransitionDefine<T, S, E, C> withAction(Action action);
}
