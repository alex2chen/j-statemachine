package com.kxtx.fsm.builder.chain;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.filter.validator.PreChecker;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public interface On<T extends StateMachine<T, S, E, C>, S, E, C> extends When<T, S, E, C> {
    When<T, S, E, C> when(PreChecker<C> checker);
    When<T, S, E, C> whenMvel(String expression);
}
