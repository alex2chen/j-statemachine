package com.github.middleware.fsm.core.factory;

import com.github.middleware.fsm.core.types.TypeReference;
import com.github.middleware.fsm.core.utils.ReflectUtil;
import com.github.middleware.fsm.core.utils.StringUtil;
import com.github.middleware.fsm.filter.wrapper.PostProcessor;
import com.github.middleware.fsm.filter.wrapper.PostProcessorProvider;
import com.google.common.collect.Maps;
import com.github.middleware.fsm.core.Singletonable;
import com.github.middleware.fsm.exception.StateMachineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/5
 */
public class DependProvider implements Singletonable {
    private static final String IMPL_CLASSNAME_SUFFIX = "Impl";
    private static final Logger LOGGER = LoggerFactory.getLogger(DependProvider.class);
    private Map<Class<?>, Class<?>> impls;
    private static DependProvider instance;

    private DependProvider() {
        impls = Maps.newConcurrentMap();
    }

    public static DependProvider getInstance() {
        if (instance == null) {
            instance = new DependProvider();
        }
        return instance;
    }

    private <T> Class<T> getClazz(Class<T> clz) {
        Class<T> impl = (Class<T>) impls.get(clz);
        return impl;
    }

    public void regedit(Class<?> clazz, Class<?> implClzz) {
        impls.put(clazz, implClzz);
    }

    private void unRegedit(Class<?> clazz) {
        impls.remove(clazz);
    }

    public void clear() {
        impls.clear();
    }

    /**
     * 实例化
     *
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T newInstance(Class<T> clz) {
        return newInstance(clz, null, null);
    }

    /**
     * 实例化泛型引用
     *
     * @param typeRef
     * @param <T>
     * @return
     */
    public <T> T newInstance(TypeReference<T> typeRef) {
        return newInstance(typeRef, null, null);
    }

    /**
     * 实例化带参
     *
     * @param typeRef
     * @param argTypes
     * @param args
     * @param <T>
     * @return
     */
    public <T> T newInstance(TypeReference<T> typeRef, Class<?>[] argTypes, Object[] args) {
        Class<T> clz = typeRef.getOrgType();
        return clz.cast(newInstance(clz, argTypes, args));
    }

    /**
     * 实例化带参
     *
     * @param clz
     * @param argTypes
     * @param args
     * @param <T>
     * @return
     */
    public <T> T newInstance(Class<T> clz, Class<?>[] argTypes, Object[] args) {
        Class<T> implementationClass = loadImplClazz(clz);
        if (args == null) {
            return postProcess(clz, ReflectUtil.newInstance(implementationClass));
        }
        Constructor<T> constructor = ReflectUtil.getConstructor(implementationClass, argTypes);
        return postProcess(clz, ReflectUtil.newInstance(constructor, args));
    }

    /**
     * 后置处理
     *
     * @param clz
     * @param component
     * @param <T>
     * @return
     */
    private <T> T postProcess(Class<T> clz, T component) {
        PostProcessor<T> postProcessor = PostProcessorProvider.getInstance().getProcessor(clz);
        if (postProcessor != null && component != null) {
            postProcessor.postProcess(component);
        }
        return component;
    }

    private <T> Class<T> loadImplClazz(Class<T> clz) {
        return loadImplClazz(clz, new HashSet<Class<?>>());
    }

    private <T> Class<T> loadImplClazz(Class<T> clz, HashSet<Class<?>> his) {
        if (!his.add(clz)) {
            throw new StateMachineException("校验异常：非法循环植入" + clz);
        }
        if (!clz.isInterface()) {
            Class<T> implObj = getClazz(clz);
            if (implObj != null && !implObj.isInterface()) {
                clz = implObj;
            }
            return clz;
        }
        Class<T> implObj = getClazz(clz);
        if (implObj == null) {
            implObj = loaderClazz(clz);
            regedit(clz, implObj);
        }
        return loadImplClazz(implObj, his);
    }

    private <T> Class<T> loaderClazz(Class<T> clz) {
        Class<?> implObj = null;
        String implClazzName = String.format("%s.support.%s%s", StringUtil.getNameByClassName(clz.getName()), clz.getSimpleName(), IMPL_CLASSNAME_SUFFIX);
        try {
            implObj = Class.forName(implClazzName);
        } catch (ClassNotFoundException ex) {
            LOGGER.error("执行Class.forName异常:", ex);
            implObj = ReflectUtil.getClass(implClazzName);
        }
        return (Class<T>) implObj;
    }
}
