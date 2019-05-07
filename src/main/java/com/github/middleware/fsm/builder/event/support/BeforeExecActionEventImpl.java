package com.github.middleware.fsm.builder.event.support;

import com.github.middleware.fsm.builder.event.BeforeExecActionEvent;
import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.exec.dto.ExecServiceContext;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/14
 */
public class BeforeExecActionEventImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractExecActionEvent<T, S, E, C> implements BeforeExecActionEvent<T, S, E, C> {

    public BeforeExecActionEventImpl(ExecServiceContext<T, S, E, C> actionContext) {
        super(actionContext);
    }
}