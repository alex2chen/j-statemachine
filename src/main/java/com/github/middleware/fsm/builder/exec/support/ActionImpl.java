package com.github.middleware.fsm.builder.exec.support;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.exception.StateMachineException;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/14
 */
public class ActionImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractAction<T, S, E, C> {
    private Action<T, S, E, C> action;

    public ActionImpl(Action<T, S, E, C> action) {
        this.action = action;
    }

    @Override
    public void execute(S from, S to, E event, C context, T stateMachine, String actionName) {
        throw new StateMachineException("未发生任何调用.");
    }
}
