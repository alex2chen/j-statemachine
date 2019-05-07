package com.github.middleware.fsm.builder.machine;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.chain.LocalTransitionBuilder;
import com.github.middleware.fsm.builder.exec.EntryExitBuilder;
import com.github.middleware.fsm.builder.plugin.PluginBuilder;
import com.github.middleware.fsm.builder.plugin.PluginProvider;
import com.github.middleware.fsm.config.application.StateMachineConfig;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public interface StateMachineBuilder<T extends StateMachine<T, S, E, C>, S, E, C> {
    LocalTransitionBuilder<T, S, E, C> localTransition();

    T newStateMachine();

    T newStateMachine(S initialStateName);

    T newStateMachine(S initialStateName, Object... extraParams);

    @Deprecated
    T newStateMachine(T fsmBean, S initialStateName, StateMachineConfig configuration, Object... extraParams);

    EntryExitBuilder<T, S, E, C> onEntry(S stateName);

    EntryExitBuilder<T, S, E, C> onExit(S stateName);

    PluginBuilder newPluginBuilder(PluginProvider pluginProvider);
}
