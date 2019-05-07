package com.github.middleware.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
public class NotSupportedException extends StateMachineException {
    public NotSupportedException(String errorMessage) {
        super(errorMessage);
    }
}
