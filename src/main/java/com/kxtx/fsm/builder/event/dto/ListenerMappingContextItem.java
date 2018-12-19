package com.kxtx.fsm.builder.event.dto;

import com.kxtx.fsm.builder.event.BaseEvent;
import com.kxtx.fsm.builder.event.BaseListener;
import com.kxtx.fsm.builder.event.EventType;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/20
 */
public class ListenerMappingContextItem implements Serializable {
    private EventType eventType;
    private Class<? extends Annotation> listenerAn;
    private Class<? extends BaseListener> listenerClz;
    private Class<? extends BaseEvent> listenerEvent;
    //监听状态机事件的函数
    private Method listenrMethod;
    private Object proxylistenerMethod;
    //触发执行函数
    private Method firePluginMethod;
    private String when;

    public ListenerMappingContextItem(Class<? extends Annotation> listenerAn, Class<? extends BaseListener> listenerClz, Class<? extends BaseEvent> listenerEvent) {
        this.listenerAn = listenerAn;
        this.listenerEvent = listenerEvent;
        this.listenerClz = listenerClz;
    }

    public Class<? extends Annotation> getListenerAn() {
        return listenerAn;
    }

    public void setListenerAn(Class<? extends Annotation> listenerAn) {
        this.listenerAn = listenerAn;
    }

    public Class<? extends BaseListener> getListenerClz() {
        return listenerClz;
    }

    public void setListenerClz(Class<? extends BaseListener> listenerClz) {
        this.listenerClz = listenerClz;
    }

    public Class<? extends BaseEvent> getListenerEvent() {
        return listenerEvent;
    }

    public void setListenerEvent(Class<? extends BaseEvent> listenerEvent) {
        this.listenerEvent = listenerEvent;
    }

    public Method getListenrMethod() {
        return listenrMethod;
    }

    public void setListenrMethod(Method listenrMethod) {
        this.listenrMethod = listenrMethod;
    }

    public Object getProxylistenerMethod() {
        return proxylistenerMethod;
    }

    public void setProxylistenerMethod(Object proxylistenerMethod) {
        this.proxylistenerMethod = proxylistenerMethod;
    }

    public Method getFirePluginMethod() {
        return firePluginMethod;
    }

    public void setFirePluginMethod(Method firePluginMethod) {
        this.firePluginMethod = firePluginMethod;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "ListenerMappingContextItem{" +
                "eventType=" + eventType +
                ", listenerAn=" + listenerAn +
                ", listenerClz=" + listenerClz +
                ", listenerEvent=" + listenerEvent +
                ", listenrMethod=" + listenrMethod +
                ", proxylistenerMethod=" + proxylistenerMethod +
                ", firePluginMethod=" + firePluginMethod +
                ", when='" + when + '\'' +
                '}';
    }
}
