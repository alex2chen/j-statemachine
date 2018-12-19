package com.kxtx.fsm.builder.machine.dto;


/**
 * 请求报文
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
@Deprecated
public abstract class RequestPayload {
    @Override
    public String toString() {
        return getRequestPayloadMsg();
    }

    public abstract String getRequestPayloadMsg();
}
