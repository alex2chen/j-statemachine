package com.kxtx.fsm.core.factory;

import com.google.common.base.Preconditions;
import com.kxtx.fsm.core.Singletonable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class SingletonProviderFactory implements Singletonable {
    private Map<String, Object> instanceRegistry = new ConcurrentHashMap<String, Object>();
    private static SingletonProviderFactory instance;

    private SingletonProviderFactory() {
    }

    public static SingletonProviderFactory getInstance() {
        if (instance == null) {
            instance = new SingletonProviderFactory();
        }
        return instance;
    }

    public <T> void register(Class<T> componentClass, Object instance) {
        Preconditions.checkArgument(componentClass.isAssignableFrom(instance.getClass()));
        instanceRegistry.put(componentClass.getName(), instance);
    }

    public void unregister(Class<?> componentClass) {
        instanceRegistry.remove(componentClass.getName());
    }

    public void clearRegistry() {
        instanceRegistry.clear();
    }

    public <T> T get(Class<T> componentClass) {
        Object instance = instanceRegistry.get(componentClass.getName());
        if (instance == null) {
            try {
                instance = DependProvider.getInstance().newInstance(componentClass);
            } catch (Exception e) {
                instance = null;
            }
            if (instance != null) {
                register(componentClass, instance);
            }
        }
        return instance != null ? componentClass.cast(instance) : null;
    }

}
