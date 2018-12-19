package com.kxtx.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/24
 */
public class SateAlreadyExistsException extends StateMachineException {
    public SateAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
