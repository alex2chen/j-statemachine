package com.kxtx.fsm.builder.event.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.AfterExecActionEvent;
import com.kxtx.fsm.builder.event.ExecActionExceptionEvent;
import com.kxtx.fsm.builder.exec.dto.ExecServiceContext;
import com.kxtx.fsm.exception.StateMachineException;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/20
 */
public class ExecActionExceptionEventImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractExecActionEvent<T, S, E, C> implements ExecActionExceptionEvent<T, S, E, C> {
    private StateMachineException e;

    public ExecActionExceptionEventImpl(ExecServiceContext<T, S, E, C> execServiceContext, StateMachineException error) {
        super(execServiceContext);
        this.e = error;
    }

    @Override
    public StateMachineException getException() {
        return e;
    }
}
