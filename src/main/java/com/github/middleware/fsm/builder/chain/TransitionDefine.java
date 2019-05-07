package com.github.middleware.fsm.builder.chain;

import com.github.middleware.fsm.builder.state.StateDefine;
import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.filter.validator.PreChecker;
import com.github.middleware.fsm.builder.exec.Action;

import java.util.List;

/**
 * 状态扭转链
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public interface TransitionDefine<T extends StateMachine<T, S, E, C>, S, E, C> extends TransitionActivity<T, S, E, C>, TransitionCriteria<T, S, E, C> {
    void setFromState(StateDefine<T, S, E, C> state);
    void setToState(StateDefine<T, S, E, C> state);
    void setPreChecker(PreChecker<C> preChecker);
    void addAction(Action action);
    void setEvent(E event);

    E getEvent();
    StateDefine<T, S, E, C> getFromState();
    StateDefine<T, S, E, C> getToState();
    PreChecker<C> getPreChecker();
    List<Action> getActionList();
    boolean isEqual(S fromState, S toState, E event);
    boolean isEqual(S fromState, S toState, E event, Class<?> checker);

}
