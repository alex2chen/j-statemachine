package com.github.middleware.fsm.builder.chain;

import com.github.middleware.fsm.builder.state.StateDefine;
import com.github.middleware.fsm.builder.state.dto.StateContext;
import com.github.middleware.fsm.builder.StateMachine;

/**
 * 扭转交互
 * Created by YT on 2017/3/8.
 */
public interface TransitionActivity<T extends StateMachine<T, S, E, C>, S, E, C> {
    /**
     * 触发扭转链检查
     *
     * @param stateContext
     */
    void fireInternal(StateContext<T, S, E, C> stateContext);

    /**
     * 执行 transition action
     *
     * @param stateContext
     * @return
     */
    StateDefine<T, S, E, C> pass(StateContext<T, S, E, C> stateContext);
}
