package com.kxtx.fsm.builder.machine.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.LinkedListMultimap;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.StateMachines;
import com.kxtx.fsm.builder.chain.dto.TransitionResult;
import com.kxtx.fsm.builder.event.*;
import com.kxtx.fsm.builder.event.dto.ListenerMappingContext;
import com.kxtx.fsm.builder.event.dto.ListenerMappingContextItem;
import com.kxtx.fsm.builder.event.support.*;
import com.kxtx.fsm.builder.exec.ActionExecService;
import com.kxtx.fsm.builder.machine.dto.StateMachineContext;
import com.kxtx.fsm.builder.machine.dto.StateMachineData;
import com.kxtx.fsm.builder.state.ImmutableLinkedState;
import com.kxtx.fsm.builder.state.dto.StateContext;
import com.kxtx.fsm.config.application.StateMachineConfig;
import com.kxtx.fsm.builder.state.StateDefine;
import com.kxtx.fsm.core.utils.JsonUtil;
import com.kxtx.fsm.core.utils.Precondition;
import com.kxtx.fsm.core.utils.ReflectUtil;
import com.kxtx.fsm.exception.NotPayloadException;
import com.kxtx.fsm.exception.NotSuchEventException;
import com.kxtx.fsm.exception.StateMachineException;
import com.kxtx.fsm.filter.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public abstract class AbstractStateMachine<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractSubject implements StateMachine<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStateMachine.class);
    private StateMachineData<T, S, E, C> data;
    private StateMachineException lastException = null;
    private StateMachineConfig config;
    private ActionExecService executor;

    public AbstractStateMachine() {
    }

    /**
     * 预处理
     *
     * @param initialState
     * @param states
     * @param config
     * @param orgData
     */
    public void prePostConstruct(S initialState, Map<S, ? extends StateDefine<T, S, E, C>> states, StateMachineConfig config, StateMachineData<T, S, E, C> orgData) {
        data = orgData;
        if (initialState != null) {
            data.initialState(initialState);
            data.currentState(initialState);
        }
        this.config = config;
        executor = StateMachines.newActionExecService(config);
        LOGGER.info("fsm.load 状态机实例化完成." + JsonUtil.json(data));
    }

    @Override
    public T getThis() {
        return (T) this;
    }

    /**
     * get identifier
     *
     * @return
     */
    @Override
    public String identifier() {
        return this.data.identifier();
    }

    /**
     * set identifier
     *
     * @return
     */
    @Override
    public void identifier(String id) {
        this.data.identifier(id);
    }

    /**
     * @return
     */
    @Override
    public Set<S> getStates() {
        return data.states();
    }

    @Override
    public S getInitialState() {
        return data.initialState();
    }

    @Override
    public StateDefine<T, S, E, C> getInitialRawState() {
        return data.initialRawState();
    }

    @Override
    public S getCurrentState() {
        if (lastException != null) throw lastException;
        return data.currentState();
    }

    @Override
    public StateDefine<T, S, E, C> getCurrentRawState() {
        return data.currentRawState();
    }

    @Override
    public S getLastState() {
        return data.lastState();
    }

    @Override
    public StateDefine<T, S, E, C> getLastRawState() {
        return data.lastRawState();
    }

    /**
     * 触发事件
     *
     * @param event
     */
    @Override
    public void handleEvent(E event) {
        handleEvent(event, null);
    }

    /**
     * 检查请求参数
     *
     * @param event
     * @param context
     */
    private void checkHanlde(E event, C context) {
        if (event != null && !data.getEventClz().isAssignableFrom(event.getClass())) {
            throw new NotSuchEventException("校验异常：请求事件类型不匹配.");
        }
        if (context != null && !data.getContextClz().isAssignableFrom(context.getClass())) {
            throw new NotPayloadException("校验异常：请求报文类型不匹配..");
        }
    }

    @Override
    public void handleEvent(S initialStateName, E event, C context) {
        if (initialStateName == null || !data.states().contains(initialStateName)) {
            if (data.initialState() != null)
                LOGGER.warn("fsm.fire 订单状态初始状态已调整(" + data.initialState() + ")->(" + initialStateName + ").");
            throw new StateMachineException("校验异常：初始化状态未正确定义！");
        }
        data.initialState(initialStateName);
        data.currentState(initialStateName);
        handleEvent(event, context);
    }

    @Override
    public void handleEvent(E event, C context) {
        checkHanlde(event, context);
//        boolean isEntryPoint = StateMachineContext.getTnstance() == null;
//        if (isEntryPoint) {
        StateMachineContext.setTnstance(getThis());
//        } else if (StateMachineContext.getTnstance() != this) {
//            T currentInstance = StateMachineContext.getTnstance();
//            currentInstance.handleEvent(event, context);
//            return;
//        }
        try {
            if (StateMachineContext.isTestEvent()) {
                fireTest(event, context);
            } else {
                LOGGER.info("fsm.fire open data:" + context);
                clearLastExecHis(data);
                processEventFlow(event, context, data);
            }
        } finally {
//            if (isEntryPoint) {
            StateMachineContext.setTnstance(null);
//            }
        }
    }

    private void clearLastExecHis(StateMachineData<T, S, E, C> data) {
        lastException = null;
    }

    @Deprecated
    private S fireTest(E event, C context) {
        S testResult = null;
        StateMachineData<T, S, E, C> cloneData = data;
        processEventFlow(event, context, cloneData);
        testResult = resolveState(cloneData.currentState(), cloneData);
        return testResult;
    }

    private S resolveState(S state, StateMachineData<T, S, E, C> localData) {
        S resolvedState = state;
        StateDefine<T, S, E, C> rawState = localData.rawStateFrom(resolvedState);
        if (rawState instanceof ImmutableLinkedState) {
            ImmutableLinkedState<T, S, E, C> linkedRawState = (ImmutableLinkedState<T, S, E, C>) rawState;
            resolvedState = linkedRawState.getLinkedStateMachine(getThis()).getCurrentState();
        }
        return resolvedState;
    }

    /**
     * 此处核心执行流
     * TODO:后期再改造下
     *
     * @param event
     * @param context
     * @param originalData
     * @return
     */
    private boolean processEventFlow(E event, C context, StateMachineData<T, S, E, C> originalData) {
        StateMachineData<T, S, E, C> localData = originalData;
        StateDefine<T, S, E, C> fromState = localData.currentRawState();
        S fromStateId = fromState.getName(), toStateId = null;
        try {
            //扭转开始
            beforeTransitionBegin(fromStateId, event, context);
            fireEvent(new TransitionBeginEventImpl<T, S, E, C>(fromStateId, event, context, getThis()));
            TransitionResult<T, S, E, C> result = new TransitionResult<T, S, E, C>(false, fromState, null);
            StateContext<T, S, E, C> stateContext = new StateContext(this, fromState, event, context, result, executor);
            fromState.fireInternal(stateContext);
            toStateId = result.getToState().getName();
            if (result.isAccepted()) {
                originalData.lastState(fromStateId);
                originalData.currentState(toStateId);
                //扭转完成
                fireEvent(new TransitionCompleteEventImpl<T, S, E, C>(fromStateId, toStateId, event, context, getThis()));
                afterTransitionCompleted(fromStateId, getCurrentState(), event, context);
                LOGGER.info("fsm.fire closed.");
                return true;
            } else {
                //扭转失败
                fireEvent(new TransitionDeclinedEventImpl<T, S, E, C>(fromStateId, event, context, getThis()));
                afterTransitionDeclined(fromStateId, event, context);
            }
        } catch (Exception e) {
            LOGGER.error("fsm.fire error:", e);
            //扭转异常
            lastException = (e instanceof StateMachineException) ? (StateMachineException) e : new StateMachineException(e.getMessage());
            fireEvent(new TransitionExceptionEventImpl<T, S, E, C>(lastException, fromStateId, localData.currentState(), event, context, getThis()));
            afterTransitionCausedException(fromStateId, toStateId, event, context);
        } finally {
            //扭转结束
            fireEvent(new TransitionEndEventImpl<T, S, E, C>(fromStateId, toStateId, event, context, getThis()));
            afterTransitionEnd(fromStateId, getCurrentState(), event, context);
        }
        LOGGER.info("fsm.fire closed..");
        return false;
    }

    @Override
    public boolean canAccept(E event) {
        StateDefine<T, S, E, C> testRawState = getCurrentRawState();
        if (testRawState == null) {
            testRawState = getInitialRawState();
        }
        return testRawState.getEvents().contains(event);
    }

    @Override
    public StateMachineException getLastException() {
        return this.lastException;
    }


    @Override
    public StateMachineConfig getConfig() {
        return config;
    }

    @Override
    protected boolean enableEventListener() {
        Precondition.checkNotNull(config, "校验异常：状态机的配置信息不能为空.");
        return config.enableEventListener();
    }

    protected void beforeTransitionBegin(S fromStateId, E event, C context) {
    }

    protected void afterTransitionDeclined(S fromStateId, E event, C context) {
    }

    protected void afterTransitionCompleted(S fromStateId, Object currentState, E event, C context) {
    }

    protected void afterTransitionCausedException(S fromStateId, S toStateId, E event, C context) {
        if (getLastException().getTargetException() != null)
            LOGGER.error("fsm.fire 触发事件发生异常：", getLastException().getTargetException());
        throw getLastException();
    }

    protected void afterTransitionEnd(S fromStateId, Object currentState, E event, C context) {
    }

    @Override
    public void beforeActionInvoked(S fromState, S toState, E event, C context) {
    }

    @Override
    public void afterActionInvoked(S fromState, S toState, E event, C context) {
    }

    /**
     * 添加各种事件监听
     *
     * @param listener
     */
    @Override
    public void addTransitionBeginListener(TransitionBeginListener<T, S, E, C> listener) {
        addListener(TransitionBeginEvent.class, listener, TransitionBeginListener.METHOD);
    }

    @Override
    public void removeTransitionBeginListener(TransitionBeginListener<T, S, E, C> listener) {
        removeListener(TransitionBeginEvent.class, listener, TransitionBeginListener.METHOD);
    }

    @Override
    public void addTransitionCompleteListener(TransitionCompleteListener<T, S, E, C> listener) {
        addListener(TransitionCompleteEvent.class, listener, TransitionCompleteListener.METHOD);
    }

    @Override
    public void removeTransitionCompleteListener(TransitionCompleteListener<T, S, E, C> listener) {
        removeListener(TransitionCompleteEvent.class, listener, TransitionCompleteListener.METHOD);
    }

    @Override
    public void addTransitionExceptionListener(TransitionExceptionListener<T, S, E, C> listener) {
        addListener(TransitionExceptionEvent.class, listener, TransitionExceptionListener.METHOD);
    }

    @Override
    public void removeTransitionExceptionListener(TransitionExceptionListener<T, S, E, C> listener) {
        removeListener(TransitionExceptionEvent.class, listener, TransitionExceptionListener.METHOD);
    }

    @Override
    public void addTransitionDeclinedListener(TransitionDeclinedListener<T, S, E, C> listener) {
        addListener(TransitionDeclinedEvent.class, listener, TransitionDeclinedListener.METHOD);
    }

    @Override
    public void removeTransitionDeclinedListener(TransitionDeclinedListener<T, S, E, C> listener) {
        removeListener(TransitionDeclinedEvent.class, listener, TransitionDeclinedListener.METHOD);
    }

    @Override
    public void addTransitionEndListener(TransitionEndListener<T, S, E, C> listener) {
        addListener(TransitionEndEvent.class, listener, TransitionEndListener.METHOD);
    }

    @Override
    public void removeTransitionEndListener(TransitionEndListener<T, S, E, C> listener) {
        removeListener(TransitionEndEvent.class, listener, TransitionEndListener.METHOD);
    }

    @Override
    public void addExecActionListener(BeforeExecActionListener<T, S, E, C> listener) {
        executor.addExecActionListener(listener);
    }

    @Override
    public void removeExecActionListener(BeforeExecActionListener<T, S, E, C> listener) {
        executor.removeExecActionListener(listener);
    }

    @Override
    public void pluginsStart() {
        for (ListenerMappingContext listenerMappingContext : data.getPlugins()) {
            List<Method> methods = ReflectUtil.getAnnotatedMethods(listenerMappingContext.getPluginProvider().getClass(), PostConstruct.class, new Class[]{String.class});
            if (methods == null || methods.size() != 1) {
                LOGGER.warn("fsm.load 插件类实现了多个PostConstruct可能不会都被执行.");
            } else {
                ReflectUtil.invoke(methods.get(0), listenerMappingContext.getPluginProvider(), new Object[]{data.identifier()});
            }

            if (listenerMappingContext.getMappingContextItems().isEmpty()) continue;
            for (ListenerMappingContextItem item : listenerMappingContext.getMappingContextItems()) {
                if (item.getEventType() == EventType.Transition) {
                    addListener(item.getListenerEvent(), item.getProxylistenerMethod(), item.getListenrMethod());
                } else {
                    executor.addProxyListener(item.getListenerEvent(), item.getProxylistenerMethod(), item.getListenrMethod());
                }
            }
        }

    }
}
