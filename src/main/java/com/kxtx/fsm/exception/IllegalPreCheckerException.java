package com.kxtx.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public class IllegalPreCheckerException extends StateMachineException {
    public IllegalPreCheckerException(String errorMessage) {
        super(errorMessage);
    }
}
