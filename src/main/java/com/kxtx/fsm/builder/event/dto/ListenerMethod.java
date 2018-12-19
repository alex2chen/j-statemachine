package com.kxtx.fsm.builder.event.dto;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.kxtx.fsm.builder.event.AsyncEventListener;
import com.kxtx.fsm.core.factory.ThreadExecutorService;
import com.kxtx.fsm.core.utils.Precondition;
import com.kxtx.fsm.core.utils.ReflectUtil;
import com.kxtx.fsm.exception.StateMachineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class ListenerMethod implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerMethod.class);
    private Class<?> eventType;
    private Object target;
    private Method method;
    private boolean hasParameter = false;
    private boolean isAsync;
    private long timeout;

    public ListenerMethod(Class<?> eventType, Object listener, Method method) {
        Preconditions.checkArgument(eventType != null && listener != null && method != null, "校验异常：入参不能为空!");
        Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(listener.getClass()), "校验异常：" + method.getName() + "不适用于 " + listener.getClass().getName());
        this.eventType = eventType;
        this.target = listener;
        this.method = method;
        this.isAsync = AsyncEventListener.class.isAssignableFrom(listener.getClass());
        this.timeout = isAsync ? AsyncEventListener.class.cast(listener).timeout() : -1;
        final Class<?>[] params = method.getParameterTypes();
        if (params.length > 0) {
            if (params.length == 1 && params[0].isAssignableFrom(eventType)) {
                hasParameter = true;
            } else {
                throw new StateMachineException("校验异常：参数类型异常！");
            }
        }
    }

    public void invokeMethod(final Object event) {
        // LOGGER.info("fsm.fire global event," + JSON.toJSONString(this));
        LOGGER.info("fsm.fire global event," + event);
        Precondition.checkState(eventType.isAssignableFrom(event.getClass()), "校验异常：类型不匹配.");
        if (isAsync) {
            ThreadExecutorService.getExecutor().execute(() -> ReflectUtil.invoke(method, target, hasParameter ? new Object[]{event} : new Object[0]));
        } else {
            synchronized (target) {
                ReflectUtil.invoke(method, target, hasParameter ? new Object[]{event} : new Object[0]);
            }
        }
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public boolean matches(Class<?> eventType, Object target) {
        return (this.target == target) && (eventType.equals(this.eventType));
    }

    public boolean matches(Class<?> eventType, Object target, Method method) {
        return (this.target == target) && (eventType.equals(this.eventType) && method.equals(this.method));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ListenerMethod t = (ListenerMethod) obj;
        return (eventType == t.eventType || (eventType != null && eventType.equals(t.eventType)))
                && (target == t.target || (target != null && target.equals(t.target)))
                && (method == t.method || (method != null && method.equals(t.method)));
    }

    @Override
    public String toString() {
        return new StringBuilder(target.getClass().getSimpleName()).
                append('.').append(method.getName()).append('(').
                append(eventType.getSimpleName()).append(')').toString();
    }

}
