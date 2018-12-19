package com.kxtx.fsm.builder.machine.support;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.kxtx.fsm.builder.event.*;
import com.kxtx.fsm.builder.event.annotation.*;
import com.kxtx.fsm.builder.event.dto.ListenerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;

/**
 * 向FSM中添加事件监听
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public abstract class AbstractSubject {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSubject.class);
    private LinkedHashSet<ListenerMethod> listeners = null;

    /**
     * 触发事件
     *
     * @param event
     */
    protected void fireEvent(BaseEvent event) {
        if (listeners == null) return;
        ListenerMethod[] listenerArray = listeners.toArray(new ListenerMethod[listeners.size()]);
        for (int i = 0; i < listenerArray.length; i++) {
            if (listenerArray[i].getEventType().isAssignableFrom(event.getClass())) {
                listenerArray[i].invokeMethod(event);
            }
        }
    }

    /**
     * 添加事件监听
     *
     * @param eventType
     * @param listener
     * @param method
     */
    protected void addListener(Class<?> eventType, Object listener, Method method) {
        register(eventType, listener, method);
    }

    /**
     * 删除监听
     *
     * @param eventType
     * @param listener
     * @param method
     */
    protected void removeListener(Class<?> eventType, Object listener, Method method) {
        if (listeners != null) {
            ListenerMethod toBeRemove = Iterators.find(listeners.iterator(), new Predicate<ListenerMethod>() {
                @Override
                public boolean apply(ListenerMethod m) {
                    return m.matches(eventType, listener, method);
                }
            });
            if (toBeRemove != null) {
                listeners.remove(toBeRemove);
            }
        }
    }

    protected void removeListener(Class<?> eventType, Object listener) {
        if (listeners != null) {
            Iterators.removeIf(listeners.iterator(), new Predicate<ListenerMethod>() {
                @Override
                public boolean apply(ListenerMethod m) {
                    return m.matches(eventType, listener);
                }
            });
        }
    }

    /**
     * 添加事件监听
     *
     * @param eventType
     * @param listener
     * @param method
     */
    private void register(Class<?> eventType, Object listener, Method method) {
        if (!enableEventListener()) {
            LOGGER.warn("addListener失败，未开启相应配置");
            return;
        }
        if (listeners == null) {
            listeners = new LinkedHashSet<ListenerMethod>();
        }
        listeners.add(new ListenerMethod(eventType, listener, method));
    }

    protected abstract boolean enableEventListener();

}
