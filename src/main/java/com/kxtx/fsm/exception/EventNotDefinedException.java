package com.kxtx.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/24
 */
public class EventNotDefinedException extends StateMachineException {
    public EventNotDefinedException(String errorMessage) {
        super(errorMessage);
    }
}
