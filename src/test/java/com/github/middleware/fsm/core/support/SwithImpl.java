package com.github.middleware.fsm.core.support;

import com.github.middleware.fsm.core.Swith;

import javax.annotation.PostConstruct;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class SwithImpl implements Swith {
    private String name;
    private String version;

    public SwithImpl() {
    }

    public SwithImpl(String name) {
        this.name = name;
    }
    @PostConstruct
    public void postConstruct() {
        this.name="..";
        version = "0.0";
    }

    @Override
    public void on() {
        System.out.println(getClass().getSimpleName() + ".on.");
    }

    @Override
    public void off() {
        System.out.println(getClass().getSimpleName() + ".off.");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getVersion() {
        return version;
    }
}
