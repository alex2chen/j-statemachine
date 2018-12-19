package com.kxtx.fsm.exception;

/**
 * 异常Base类
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/24
 */
public class StateMachineException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
    private Throwable targetException;

    public StateMachineException() {
    }

    public StateMachineException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public StateMachineException(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = String.format("%08d : %s.", getErrorCode(), errorCode.getDescription());
    }

    public StateMachineException(ErrorCode errorCode, Object... parameters) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = String.format("%08d : %s.", getErrorCode(),
                String.format(errorCode.getDescription(), parameters));
    }

    public StateMachineException(Throwable targetException, ErrorCode errorCode, Object... parameters) {
        this(errorCode, parameters);
        this.targetException = targetException;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Throwable getTargetException() {
        return targetException;
    }

    public void setTargetException(Throwable targetException) {
        this.targetException = targetException;
    }

    @Override
    public String toString() {
        return "StateMachineException{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", targetException=" + targetException +
                '}';
    }
}
