package com.github.middleware.fsm.builder.state;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.state.dto.StateContext;

/**
 * 状态进入/退出
 * Created by YT on 2017/3/8.
 */
public interface StateActivity<T extends StateMachine<T, S, E, C>, S, E, C> {
    /**
     * 触发状态监测
     *
     * @param stateContext
     */
    void fireInternal(StateContext<T, S, E, C> stateContext);

    /**
     * 执行exit action
     *
     * @param stateContext
     */
    void exit(StateContext<T, S, E, C> stateContext);

    /**
     * 执行entry action
     *
     * @param stateContext
     */
    void enter(StateContext<T, S, E, C> stateContext);
}
