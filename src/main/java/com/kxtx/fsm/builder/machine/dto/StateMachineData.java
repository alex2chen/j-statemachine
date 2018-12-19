package com.kxtx.fsm.builder.machine.dto;

import com.google.common.collect.Lists;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.dto.ListenerMappingContext;
import com.kxtx.fsm.builder.state.StateDefine;
import com.kxtx.fsm.core.el.MvelScriptManager;

import java.io.Serializable;
import java.util.*;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class StateMachineData<T extends StateMachine<T, S, E, C>, S, E, C> implements Serializable, Cloneable {
    private Map<S, StateDefine<T, S, E, C>> orgStates;
    private Class<? extends T> stateMachineClz;
    private Class<S> stateClz;
    private Class<E> eventClz;
    private Class<C> contextClz;
    private Class<?>[] extraParamTypes;
    private MvelScriptManager scriptManager;

    private S currentState;
    private S lastState;
    private S initialState;
    private String identifier;
    private List<ListenerMappingContext> plugins;

    public StateMachineData(Map<S, StateDefine<T, S, E, C>> states) {
        this(states, null);
    }

    public StateMachineData(Map<S, StateDefine<T, S, E, C>> states, Class<? extends T> stateMachineClz) {
        this.orgStates = Collections.unmodifiableMap(states);
        this.stateMachineClz = stateMachineClz;
        this.plugins = Lists.newArrayList();
    }

    public MvelScriptManager getScriptManager() {
        return scriptManager;
    }

    public void setScriptManager(MvelScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    public Class<?>[] getExtraParamTypes() {
        return extraParamTypes;
    }

    public void setExtraParamTypes(Class<?>[] extraParamTypes) {
        this.extraParamTypes = extraParamTypes;
    }

    public Class<C> getContextClz() {
        return contextClz;
    }

    public void setContextClz(Class<C> contextClz) {
        this.contextClz = contextClz;
    }

    public Class<E> getEventClz() {
        return eventClz;
    }

    public void setEventClz(Class<E> eventClz) {
        this.eventClz = eventClz;
    }

    public Class<S> getStateClz() {
        return stateClz;
    }

    public void setStateClz(Class<S> stateClz) {
        this.stateClz = stateClz;
    }

    public Class<? extends T> getStateMachineClz() {
        return stateMachineClz;
    }

    public void setStateMachineClz(Class<? extends T> stateMachineClz) {
        this.stateMachineClz = stateMachineClz;
    }

    public void initialState(S initialStateId) {
        this.initialState = initialStateId;
    }

    public void currentState(S currentStateId) {
        this.currentState = currentStateId;
    }

    public void lastState(S lastStateId) {
        this.lastState = lastStateId;
    }

    public S currentState() {
        return currentState;
    }

    public S getCurrentState() {
        return currentState;
    }

    public S lastState() {
        return lastState;
    }

    public S getLastState() {
        return lastState;
    }

    public S initialState() {
        return initialState;
    }

    public S getInitialState() {
        return initialState;
    }

    public void identifier(String id) {
        this.identifier = id;
    }

    public String identifier() {
        return identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Map<S, StateDefine<T, S, E, C>> getOrgStates() {
        if (orgStates == null) {
            return Collections.emptyMap();
        }
        return orgStates;
    }

    public Set<S> states() {
        return getOrgStates().keySet();
    }

    public Collection<StateDefine<T, S, E, C>> rawStates() {
        return orgStates.values();
    }

    public StateDefine<T, S, E, C> currentRawState() {
        return currentState != null ? rawStateFrom(currentState) : null;
    }

    public StateDefine<T, S, E, C> initialRawState() {
        return rawStateFrom(initialState());
    }

    public StateDefine<T, S, E, C> lastRawState() {
        return rawStateFrom(lastState);
    }

    public StateDefine<T, S, E, C> rawStateFrom(S state) {
        if (state == null) return null;
        StateDefine<T, S, E, C> rawState = getOrgStates().get(state);
        return rawState != null ? rawState.self() : null;
    }

    public List<ListenerMappingContext> getPlugins() {
        return plugins;
    }

    public void addPlugin(ListenerMappingContext plugin) {
        if (plugin != null) this.plugins.add(plugin);
    }

    @Override
    public String toString() {
        return "StateMachineData{" +
                "orgStates=" + orgStates +
                ", stateMachineClz=" + stateMachineClz +
                ", stateClz=" + stateClz +
                ", eventClz=" + eventClz +
                ", contextClz=" + contextClz +
                ", extraParamTypes=" + Arrays.toString(extraParamTypes) +
                ", scriptManager=" + scriptManager +
                ", currentState=" + currentState +
                ", lastState=" + lastState +
                ", initialState=" + initialState +
                ", identifier='" + identifier + '\'' +
                ", plugins=" + plugins +
                '}';
    }
}
