package com.kxtx.fsm.builder.exec.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.exec.Action;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public abstract class AbstractAction<T extends StateMachine<T, S, E, C>, S, E, C> implements Action<T, S, E, C> {

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_Action";
    }

    @Override
    public int weight() {
        return WEIGHT_NORMAL;
    }

    @Override
    final public String toString() {
        return "instance#" + getClass().getName();
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public long timeout() {
        return -1;
    }
}