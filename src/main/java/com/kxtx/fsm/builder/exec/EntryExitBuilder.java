package com.kxtx.fsm.builder.exec;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.chain.When;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public interface EntryExitBuilder<T extends StateMachine<T, S, E, C>, S, E, C> extends When<T, S, E, C> {
}
