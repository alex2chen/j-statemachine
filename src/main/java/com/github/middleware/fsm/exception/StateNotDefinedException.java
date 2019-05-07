package com.github.middleware.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/24
 */
public class StateNotDefinedException extends StateMachineException {
    public StateNotDefinedException(String errorMessage) {
        super(errorMessage);
    }
}
