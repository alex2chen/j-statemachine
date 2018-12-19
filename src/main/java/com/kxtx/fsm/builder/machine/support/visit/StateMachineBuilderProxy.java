package com.kxtx.fsm.builder.machine.support.visit;

import com.kxtx.fsm.builder.machine.StateMachineBuilder;
import com.kxtx.fsm.config.application.StateMachineConfig;

/**
 * Created by YT on 2017/3/12.
 */
public interface StateMachineBuilderProxy extends StateMachineBuilder<StateMachineProxy, Object, Object, Object> {
    <T extends StateMachineProxy> T newDefaultStateMachine(Object initialStateId);

    <T extends StateMachineProxy> T newDefaultStateMachine(Object initialStateId, Object... extraParams);

    <T extends StateMachineProxy> T newDefaultStateMachine(
            Object initialStateId, StateMachineConfig configuration, Object... extraParams);

    <T> T newSoftStateMachine(Object initialStateId);

    <T> T newSoftStateMachine(Object initialStateId, Object... extraParams);

    <T> T newSoftStateMachine(Object initialStateId, StateMachineConfig configuration, Object... extraParams);
}
