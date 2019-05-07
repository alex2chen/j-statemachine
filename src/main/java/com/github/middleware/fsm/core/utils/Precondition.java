package com.github.middleware.fsm.core.utils;

import com.github.middleware.fsm.exception.StateMachineException;

/**
 * 来源于guava
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/10
 */
public final class Precondition {
    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new StateMachineException(String.valueOf(errorMessage));
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new StateMachineException(String.valueOf(errorMessage));
        }
    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new StateMachineException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }
}
