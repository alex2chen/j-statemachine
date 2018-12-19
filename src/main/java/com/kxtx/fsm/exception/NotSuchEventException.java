package com.kxtx.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class NotSuchEventException extends StateMachineException{
    public NotSuchEventException(String errorMessage) {
        super(errorMessage);
    }
}
