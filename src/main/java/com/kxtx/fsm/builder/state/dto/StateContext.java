package com.kxtx.fsm.builder.state.dto;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.chain.dto.TransitionResult;
import com.kxtx.fsm.builder.exec.ActionExecService;
import com.kxtx.fsm.builder.state.StateDefine;

import java.io.Serializable;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class StateContext<T extends StateMachine<T, S, E, C>, S, E, C> implements Serializable {
    private StateMachine<T, S, E, C> stateMachine;
    private StateDefine<T, S, E, C> fromState;
    private C context;
    private E event;
    private TransitionResult<T, S, E, C> result;
    private ActionExecService<T, S, E, C> executor;

    public StateContext(StateMachine<T, S, E, C> stateMachine,
                        StateDefine<T, S, E, C> fromState, E event, C context,
                        TransitionResult<T, S, E, C> result, ActionExecService<T, S, E, C> executor) {
        this.stateMachine = stateMachine;
        this.fromState = fromState;
        this.event = event;
        this.context = context;
        this.result = result;
        this.executor = executor;
    }

    public StateMachine<T, S, E, C> getStateMachine() {
        return stateMachine;
    }

    public StateDefine<T, S, E, C> getFromState() {
        return fromState;
    }

    public C getContext() {
        return context;
    }

    public E getEvent() {
        return event;
    }

    public TransitionResult<T, S, E, C> getResult() {
        return result;
    }

    public ActionExecService<T, S, E, C> getExecutor() {
        return executor;
    }
}

