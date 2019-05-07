package com.github.middleware.fsm.builder.exec.support;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.exec.Action;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class ActionWrapper<T extends StateMachine<T, S, E, C>, S, E, C> implements Action<T, S, E, C> {
    private Action<T, S, E, C> delegator;

    public ActionWrapper(Action<T, S, E, C> delegator) {
        this.delegator = delegator;
    }

    @Override
    public void execute(S from, S to, E event, C context, T stateMachine,String actionName) {
        delegator.execute(from, to, event, context, stateMachine,actionName);
    }

    @Override
    public String getName() {
        return delegator.getName();
    }

    @Override
    public int weight() {
        return delegator.weight();
    }

    @Override
    public long timeout() {
        return delegator.timeout();
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}
