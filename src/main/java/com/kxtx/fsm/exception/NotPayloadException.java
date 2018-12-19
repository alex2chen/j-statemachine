package com.kxtx.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class NotPayloadException extends StateMachineException {
    public NotPayloadException(String errorMessage) {
        super(errorMessage);
    }
}
