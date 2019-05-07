package com.github.middleware.fsm.builder.chain.dto;

import com.github.middleware.fsm.builder.state.StateDefine;
import com.google.common.collect.Lists;
import com.github.middleware.fsm.builder.StateMachine;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class TransitionResult<T extends StateMachine<T, S, E, C>, S, E, C> implements Serializable {
    private boolean accepted;
    private StateDefine<T, S, E, C> toState;
    private TransitionResult<T, S, E, C> parent;
    private List<TransitionResult<T, S, E, C>> subResults;

    public TransitionResult(boolean accepted, StateDefine<T, S, E, C> toState, TransitionResult<T, S, E, C> parent) {
        this.accepted = accepted;
        this.toState = toState;
        this.parent = parent;
    }

    private void addSubResult(TransitionResult<T, S, E, C> subResult) {
        if (subResults == null)
            subResults = Lists.newArrayList();
        subResults.add(subResult);
    }

    public boolean isAccepted() {
        if (accepted) {
            return true;
        } else if (subResults != null) {
            for (TransitionResult<T, S, E, C> subResult : getSubResults()) {
                if (subResult.isAccepted()) return true;
            }
        }
        return false;
    }

    public TransitionResult<T, S, E, C> setAccepted(boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public StateDefine<T, S, E, C> getToState() {
        return toState;
    }

    public TransitionResult<T, S, E, C> setToState(StateDefine<T, S, E, C> toState) {
        this.toState = toState;
        return this;
    }

    public List<TransitionResult<T, S, E, C>> getSubResults() {
        return subResults != null ? Lists.newArrayList(subResults) :
                Collections.<TransitionResult<T, S, E, C>>emptyList();
    }

    public TransitionResult<T, S, E, C> setParent(TransitionResult<T, S, E, C> parent) {
        this.parent = parent;
        if (parent != null && parent instanceof TransitionResult) {
            ((TransitionResult<T, S, E, C>) parent).addSubResult(this);
        }
        return this;
    }

    public TransitionResult<T, S, E, C> getParentResut() {
        return parent;
    }
}
