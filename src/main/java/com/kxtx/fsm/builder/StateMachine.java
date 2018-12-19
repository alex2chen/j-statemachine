package com.kxtx.fsm.builder;

import com.google.common.collect.LinkedListMultimap;
import com.kxtx.fsm.builder.event.*;
import com.kxtx.fsm.builder.event.dto.ListenerMappingContext;
import com.kxtx.fsm.builder.plugin.PluginProvider;
import com.kxtx.fsm.builder.state.StateDefine;
import com.kxtx.fsm.config.application.StateMachineConfig;
import com.kxtx.fsm.exception.StateMachineException;

import java.util.List;
import java.util.Set;

/**
 * 有限状态机定义
 *
 * @param <T> 状态机
 * @param <S> 状态源：String/Enum
 * @param <E> 触发事件：String/Enum
 * @param <C> 携带报文
 */
public interface StateMachine<T extends StateMachine<T, S, E, C>, S, E, C> {
    /**
     * 触发事件
     *
     * @param event
     */
    void handleEvent(E event);

    void handleEvent(E event, C context);
    void handleEvent(S initialStateName,E event, C context);
    /**
     * 测试状态扭转，并返回结果
     *
     * @param event
     * @param context
     * @return
     */
    //S test(E event, C context);

    /**
     * 测试事件是否有效
     *
     * @param event
     * @return
     */
    boolean canAccept(E event);

    /**
     * 状态机标示
     *
     * @return
     */
    String identifier();

    void identifier(String id);

    /**
     * 状态机的实例
     *
     * @return
     */
    StateMachine getThis();

    /**
     * 获取所有状态
     *
     * @return
     */
    Set<S> getStates();

    /**
     * 获取初始化状态
     *
     * @return
     */
    S getInitialState();

    StateDefine<T, S, E, C> getInitialRawState();

    /**
     * 获取当前状态
     *
     * @return
     */
    S getCurrentState();

    StateDefine<T, S, E, C> getCurrentRawState();

    /**
     * 获取最终状态
     *
     * @return
     */
    S getLastState();

    StateDefine<T, S, E, C> getLastRawState();

    /**
     * 获取执行异常信息
     *
     * @return
     */
    StateMachineException getLastException();

    StateMachineConfig getConfig();
    /**
     * transition action
     *
     * @param fromState
     * @param toState
     * @param event
     * @param context
     */
    void beforeActionInvoked(S fromState, S toState, E event, C context);

    void afterActionInvoked(S fromState, S toState, E event, C context);

    /**
     * 添加各种listener
     *
     * @param listener
     */
    void addTransitionBeginListener(TransitionBeginListener<T, S, E, C> listener);

    void removeTransitionBeginListener(TransitionBeginListener<T, S, E, C> listener);

    void addTransitionCompleteListener(TransitionCompleteListener<T, S, E, C> listener);

    void removeTransitionCompleteListener(TransitionCompleteListener<T, S, E, C> listener);

    void addTransitionExceptionListener(TransitionExceptionListener<T, S, E, C> listener);

    void removeTransitionExceptionListener(TransitionExceptionListener<T, S, E, C> listener);

    void addTransitionDeclinedListener(TransitionDeclinedListener<T, S, E, C> listener);

    void removeTransitionDeclinedListener(TransitionDeclinedListener<T, S, E, C> listener);

    void addTransitionEndListener(TransitionEndListener<T, S, E, C> listener);

    void removeTransitionEndListener(TransitionEndListener<T, S, E, C> listener);

    void addExecActionListener(BeforeExecActionListener<T, S, E, C> listener);

    void removeExecActionListener(BeforeExecActionListener<T, S, E, C> listener);

    /**
     * 启动各种插件支持
     */
    void pluginsStart();
    /**
     * 触发事件执行流
     *
     * @param fromStateId
     * @param event
     * @param context
     */
    // void beforeTransitionBegin(S fromStateId, E event, C context);
    // void afterTransitionDeclined(S fromStateId, E event, C context);
    // void afterTransitionCompleted(S fromStateId, Object currentState, E event, C context);
    // void afterTransitionCausedException(S fromStateId, S toStateId, E event, C context);
    // void afterTransitionEnd(S fromStateId, Object currentState, E event, C context);
}
