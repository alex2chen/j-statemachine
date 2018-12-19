package com.kxtx.fsm.config.loader;

import com.kxtx.fsm.builder.event.dto.ListenerMappingContext;
import com.kxtx.fsm.builder.plugin.PluginProvider;
import com.kxtx.fsm.config.annotation.State;
import com.kxtx.fsm.config.annotation.Transition;

import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public interface AnnotationResovler {

    List<State> processStateLoader(Class<?> stateMachineClz);

    List<Transition> processTransitionLoader(Class<?> stateMachineClz);

    ListenerMappingContext processEventListener(PluginProvider providerListener);
}
