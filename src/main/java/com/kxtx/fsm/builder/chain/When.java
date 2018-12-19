package com.kxtx.fsm.builder.chain;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.exec.Action;
import com.kxtx.fsm.core.el.Dsl;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public interface When<T extends StateMachine<T, S, E, C>, S, E, C> extends Dsl {
    /**
     * 完成
     *
     * @param action
     */
    void complete(Action<T, S, E, C> action);
    void complete(String callMethod);
}
