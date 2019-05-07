package com.github.middleware.fsm.builder.chain.support;

import com.github.middleware.fsm.builder.chain.*;
import com.github.middleware.fsm.builder.state.StateDefine;
import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.StateMachines;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.builder.exec.dto.RuleExecContext;
import com.github.middleware.fsm.builder.chain.TransitionDefine;
import com.github.middleware.fsm.filter.validator.PreChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class LocalTransitionBuilderImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements LocalTransitionBuilder<T, S, E, C>, From<T, S, E, C>, To<T, S, E, C>, On<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalTransitionBuilderImpl.class);
    private Map<S, StateDefine<T, S, E, C>> states;
    private RuleExecContext execContext;
    private StateDefine<T, S, E, C> from;
    private StateDefine<T, S, E, C> to;
    private TransitionDefine<T, S, E, C> transition;


    public LocalTransitionBuilderImpl(Map<S, StateDefine<T, S, E, C>> states, RuleExecContext execContext) {
        this.states = states;
        this.execContext = execContext;
    }

    @Override
    public From<T, S, E, C> from(S state) {
        from = StateMachines.getState(states, state);
        return this;
    }

    @Override
    public To<T, S, E, C> to(S stateId) {
        to = StateMachines.getState(states, stateId);
        return this;
    }

    @Override
    public To<T, S, E, C> toEnd(S stateId) {
        to = StateMachines.getState(states, stateId);
        if (!to.isEndState()) {
            to.setEndState(true);
        }
        return this;
    }

    @Override
    public On<T, S, E, C> on(E event) {
        transition = from.addTransitionOn(event);
        transition.setToState(to);
        LOGGER.debug("fsm.load add Transition On:" + from.getName() + ">" + to.getName() + ">" + event);
        return this;
    }

    @Override
    public When<T, S, E, C> when(PreChecker<C> checker) {
        transition.setPreChecker(checker);
        return this;
    }

    @Override
    public When<T, S, E, C> whenMvel(String expression) {
        PreChecker<C> checker = StateMachines.newMvelCondition(expression, execContext.getScriptManager());
        transition.setPreChecker(checker);
        return this;
    }

    @Override
    public void complete(Action<T, S, E, C> action) {
        LOGGER.debug("fsm.load add Action:" + action.getName());
        transition.addAction(action);
    }

    @Override
    public void complete(String callMethod) {
        Action<T, S, E, C> action = StateMachines.newMethodCallActionProxy(callMethod, execContext);
        transition.addAction(action);
    }
}
