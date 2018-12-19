package com.kxtx.fsm.builder.exec.dto;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.exec.Action;
import com.kxtx.fsm.builder.machine.support.AbstractStateMachine;

import java.io.Serializable;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/14
 */
public class ExecServiceContext<T extends StateMachine<T, S, E, C>, S, E, C> implements Serializable {
    private String execServiceName;
    private Action<T, S, E, C> action;
    private S from;
    S to;
    E event;
    C context;
    private T stateMachine;

    public ExecServiceContext() {
    }

    public S getFrom() {
        return from;
    }

    public S getTo() {
        return to;
    }

    public E getEvent() {
        return event;
    }

    public C getContext() {
        return context;
    }

    public T getStateMachine() {
        return stateMachine;
    }

    public Action<T, S, E, C> getAction() {
        return action;
    }

    public ExecServiceContext setExecServiceName(String execServiceName) {
        this.execServiceName = execServiceName;
        return this;
    }

    public ExecServiceContext setAction(Action<T, S, E, C> action) {
        this.action = action;
        return this;
    }

    public ExecServiceContext setFrom(S from) {
        this.from = from;
        return this;
    }

    public ExecServiceContext setTo(S to) {
        this.to = to;
        return this;
    }

    public ExecServiceContext setEvent(E event) {
        this.event = event;
        return this;
    }

    public ExecServiceContext setContext(C context) {
        this.context = context;
        return this;
    }

    public ExecServiceContext setStateMachine(T stateMachine) {
        this.stateMachine = stateMachine;
        return this;
    }

    /**
     * 执行
     * TODO:有的污染需改造
     */
    public void doAction() {
        AbstractStateMachine<T, S, E, C> fsmImpl = (AbstractStateMachine<T, S, E, C>) stateMachine;
        fsmImpl.beforeActionInvoked(from, to, event, context);
        action.execute(from, to, event, context, stateMachine,execServiceName);

        fsmImpl.afterActionInvoked(from, to, event, context);
    }
}
