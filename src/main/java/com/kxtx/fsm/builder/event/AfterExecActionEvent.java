package com.kxtx.fsm.builder.event;

import com.kxtx.fsm.builder.StateMachine;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface AfterExecActionEvent<T extends StateMachine<T, S, E, C>, S, E, C> extends ActionEvent<T, S, E, C> {
}
