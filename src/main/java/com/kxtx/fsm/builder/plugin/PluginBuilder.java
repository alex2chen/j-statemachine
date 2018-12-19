package com.kxtx.fsm.builder.plugin;

import com.google.common.collect.LinkedListMultimap;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.dto.ListenerMappingContext;

import java.util.List;

/**
 * Created by YT on 2017/3/17.
 */
public interface PluginBuilder<T extends StateMachine<T, S, E, C>, S, E, C> extends PluginLiftcycle {
}
