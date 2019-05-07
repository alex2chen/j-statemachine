package com.github.middleware.fsm.builder.exec;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.exec.support.AbstractAction;

/**
 * 执行动作，包含exit、entry、action
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public interface Action<T extends StateMachine<T, S, E, C>, S, E, C> {
    int WEIGHT_NORMAL = 0;
    int WEIGHT_BEFORE = 100;
    int WEIGHT_AFTER = -100;
    int WEIGHT_IGNORE = 1000;

    void execute(S from, S to, E event, C context, T stateMachine, String actionName);

    String getName();

    /**
     * 执行权重
     *
     * @return
     */
    @Deprecated
    int weight();

    /**
     * 异步
     *
     * @return
     */
    @Deprecated
    boolean isAsync();

    /**
     * 异步超时
     *
     * @return
     */
    @Deprecated
    long timeout();


    public final static Action DUMMY_ACTION = new AbstractAction() {
        @Override
        public void execute(Object from, Object to, Object event,
                            Object context, StateMachine stateMachine, String serviceName) {
            // DO NOTHING
        }

        @Override
        public String getName() {
            return "__DUMMY_ACTION";
        }
    };
}
