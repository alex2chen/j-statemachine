package com.kxtx.fsm.core;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface Swith {
    void on();

    void off();

    void setName(String name);

    String getName();

    void setVersion(String version);

    String getVersion();
}
