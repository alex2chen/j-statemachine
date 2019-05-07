package com.github.middleware.fsm.builder.plugin;

import com.github.middleware.fsm.builder.StateMachine;

/**
 * Created by YT on 2017/3/17.
 */
public interface PluginBuilder<T extends StateMachine<T, S, E, C>, S, E, C> extends PluginLiftcycle {
}
