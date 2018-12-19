package com.kxtx.fsm.builder.exec.dto;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.exec.Action;

import java.io.Serializable;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class RegularTransition<T extends StateMachine<T, S, E, C>, S, E, C> implements Serializable {
    private List<? extends Action<T, S, E, C>> actions;

    private S from;

    private S to;

    private E event;

    public RegularTransition(S from, S to, E event) {
        this.from = from;
        this.to = to;
        this.event = event;
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

    public List<? extends Action<T, S, E, C>> getActions() {
        return actions;
    }

    public void setActions(List<? extends Action<T, S, E, C>> actions) {
        this.actions = actions;
    }
}
