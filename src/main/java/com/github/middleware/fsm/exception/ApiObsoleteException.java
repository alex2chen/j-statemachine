package com.github.middleware.fsm.exception;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class ApiObsoleteException extends StateMachineException {
    public ApiObsoleteException(String errorMessage) {
        super(errorMessage);
    }
}
