package com.github.middleware.fsm.builder.exec.support;

import com.github.middleware.fsm.builder.exec.dto.RuleExecContext;
import com.github.middleware.fsm.builder.state.StateDefine;
import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.StateMachines;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.builder.exec.EntryExitBuilder;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class EntryExitBuilderImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements EntryExitBuilder<T, S, E, C> {
    private boolean isEntryAction;
    private StateDefine<T, S, E, C> state;
    private RuleExecContext execContext;

    public EntryExitBuilderImpl() {

    }

    public EntryExitBuilderImpl(StateDefine<T, S, E, C> state, boolean isEntryAction, RuleExecContext execContext) {
        this.isEntryAction = isEntryAction;
        this.state = state;
        this.execContext = execContext;
    }

    @Override
    public void complete(Action<T, S, E, C> action) {
        if (isEntryAction) {
            state.addEntryAction(action);
        } else {
            state.addExitAction(action);
        }
    }
    @Override
    public void complete(String callMethod) {
        Action<T, S, E, C> action = StateMachines.newMethodCallActionProxy(callMethod, execContext);
        complete(action);
    }
}
