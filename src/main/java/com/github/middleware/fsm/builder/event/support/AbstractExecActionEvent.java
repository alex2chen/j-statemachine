package com.github.middleware.fsm.builder.event.support;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.event.ActionEvent;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.builder.exec.dto.ExecServiceContext;
import com.github.middleware.fsm.builder.exec.support.ActionImpl;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/14
 */
public abstract class AbstractExecActionEvent<T extends StateMachine<T, S, E, C>, S, E, C> implements ActionEvent<T, S, E, C> {
    private ExecServiceContext<T, S, E, C> execServiceContext;
    private int pos;
    private int size;

    public AbstractExecActionEvent(ExecServiceContext<T, S, E, C> execServiceContext) {
        this.execServiceContext = execServiceContext;
    }

    public AbstractExecActionEvent(int pos, int size, ExecServiceContext<T, S, E, C> execServiceContext) {
        this.pos = pos;
        this.size = size;
        this.execServiceContext = execServiceContext;
    }

    @Override
    public Action<T, S, E, C> getExecutionTarget() {
        // user can only read action info but cannot invoke action in the listener method
        return new ActionImpl<T, S, E, C>(execServiceContext.getAction());
    }

    @Override
    public S getFrom() {
        return execServiceContext.getFrom();
    }

    @Override
    public S getTo() {
        return execServiceContext.getTo();
    }

    @Override
    public E getEvent() {
        return execServiceContext.getEvent();
    }

    @Override
    public C getContext() {
        return execServiceContext.getContext();
    }

    @Override
    public T getStateMachine() {
        return execServiceContext.getStateMachine();
    }

    @Override
    public int[] getMOfN() {
        return new int[]{pos, size};
    }
}
