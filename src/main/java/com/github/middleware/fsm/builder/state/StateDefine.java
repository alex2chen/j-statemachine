package com.github.middleware.fsm.builder.state;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.chain.TransitionDefine;

import java.util.List;
import java.util.Set;

/**
 * 状态定义
 *
 * @param <S> 状态值
 * @param <E> 事件值
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public interface StateDefine<T extends StateMachine<T, S, E, C>, S, E, C> extends StateLiftcycle<T, S, E, C> {
    void setLevel(int level);
    void setParentState(StateDefine<T, S, E, C> parent);
    void setInitialState(StateDefine<T, S, E, C> childInitialState);
    void setEndState(boolean endState);
    void addChildState(StateDefine<T, S, E, C> childState);
    //向from节点添加扭转链(针对on语法)
    TransitionDefine<T, S, E, C> addTransitionOn(E event);
    void check();

    //todo:
    S getName();
    int getLevel();
    StateDefine<T, S, E, C> getParentState();
    StateDefine<T, S, E, C> getInitialState();
    boolean isEndState();
    List<StateDefine<T, S, E, C>> getChildStates();
    boolean hasChildStates();
    boolean isRootState();
    List<TransitionDefine<T, S, E, C>> getTransitions();
    List<TransitionDefine<T, S, E, C>> getTransitions(E event);
    StateDefine<T, S, E, C> self();
    String getPath();
    Set<E> getEvents();
}
