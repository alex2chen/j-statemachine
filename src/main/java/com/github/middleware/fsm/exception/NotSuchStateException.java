package com.github.middleware.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public class NotSuchStateException extends StateMachineException {
    public NotSuchStateException(String errorMessage) {
        super(errorMessage);
    }
}
