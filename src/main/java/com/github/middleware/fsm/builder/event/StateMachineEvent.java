package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.StateMachine;

/**
 * 状态机事件:
 * TODO:赞未使用
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface StateMachineEvent<T extends StateMachine<T, S, E, C>, S, E, C> extends BaseEvent {
    T getStateMachine();
}
