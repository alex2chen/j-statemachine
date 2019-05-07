package com.github.middleware.fsm.builder.machine.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * 状态机上下文环境
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public class StateMachineContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineContext.class);
    private Object currentInstance;
    private boolean isTestEvent;

    private StateMachineContext() {
    }

    public StateMachineContext(Object stateMachine, boolean isTestEvent) {
        this.currentInstance = stateMachine;
        this.isTestEvent = isTestEvent;
    }

    private static final ThreadLocal<Stack<StateMachineContext>> contextContainer = new ThreadLocal<Stack<StateMachineContext>>() {
        @Override
        protected Stack<StateMachineContext> initialValue() {
            return new Stack<StateMachineContext>();
        }
    };

    public static void setTnstance(Object instance) {
        setTnstance(instance, false);
    }

    public static void setTnstance(Object instance, boolean isTestEvent) {
        if (instance == null) {
            contextContainer.get().pop();
        } else {
            contextContainer.get().push(new StateMachineContext(instance, isTestEvent));
        }
    }

    public static <T> T getTnstance() {
        return contextContainer.get().size() > 0 ? (T) contextContainer.get().peek().currentInstance : null;
    }

    public static boolean isTestEvent() {
        return contextContainer.get().size() > 0 ? contextContainer.get().peek().isTestEvent : false;
    }

    public static void removeContext() {
        contextContainer.remove();
    }
}
