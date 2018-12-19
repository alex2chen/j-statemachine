package com.kxtx.fsm.builder.event.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.AfterExecActionEvent;
import com.kxtx.fsm.builder.exec.dto.ExecServiceContext;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/20
 */
public class AfterExecActionEventImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractExecActionEvent<T, S, E, C> implements AfterExecActionEvent<T, S, E, C> {
    public AfterExecActionEventImpl(ExecServiceContext<T, S, E, C> execServiceContext) {
        super(execServiceContext);
    }
}
