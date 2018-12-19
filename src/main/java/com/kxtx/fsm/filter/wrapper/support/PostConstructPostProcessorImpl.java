package com.kxtx.fsm.filter.wrapper.support;

import com.kxtx.fsm.filter.annotation.PostConstruct;
import com.kxtx.fsm.core.utils.ReflectUtil;
import com.kxtx.fsm.filter.wrapper.PostProcessor;

import java.lang.reflect.Method;

/**
 * 后置处理器（）
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class PostConstructPostProcessorImpl implements PostProcessor<Object> {
    @Override
    public void postProcess(final Object component) {
        Class<?> componentClass = component.getClass();
        ReflectUtil.doWithMethods(componentClass, new ReflectUtil.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return ReflectUtil.isAnnotatedWith(method, PostConstruct.class) &&
                        (method.getParameterTypes() == null || method.getParameterTypes().length == 0);
            }
        }, new ReflectUtil.MethodCallback() {
            @Override
            public void doWith(Method method) {
                ReflectUtil.invoke(method, component);
            }
        });
    }
}
