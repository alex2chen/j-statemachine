package com.kxtx.fsm.builder.exec.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.config.application.StateMachineConfig;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class ActionExecServiceImpl<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractExecService<T, S, E, C> {
    public ActionExecServiceImpl(StateMachineConfig config) {
        super(config);
    }
}
