package com.kxtx.fsm.exception;

/**
 * 异常消息清单
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/24
 */
public enum ErrorCode {
    NOT_ALLOW_ACCESS_FIELD(10002, "wasn't allowed to get field '%s.%s'"),
    NOT_ALLOW_ACCESS_METHOD(10004, "wasn't allowed to get method '%s.%s(%s)'"),
    METHOD_NOT_FOUND(10006, "couldn't find method '%s.%s(%s)'"),
    FIELD_NOT_FOUND(10008, "couldn't find field '%s.%s'"),
    ILLEGAL_CLASS_NAME(10010, "illegal class name"),
    METHOD_INVOKE_ERROR(10012, "couldn't invoke '%s' with %s on %s: %s");
    private int code;
    private String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    }
