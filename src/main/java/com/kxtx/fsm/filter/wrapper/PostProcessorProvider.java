package com.kxtx.fsm.filter.wrapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kxtx.fsm.core.factory.DependProvider;
import com.kxtx.fsm.core.utils.Precondition;
import com.kxtx.fsm.filter.wrapper.support.ComposPostProcessorImpl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public class PostProcessorProvider {
    private static PostProcessorProvider instance;
    private Map<Class<?>, PostProcessor<?>> process;
    private static final String POSTPROCESS_NAME = "postProcess";

    private PostProcessorProvider() {
        process = Maps.newConcurrentMap();
    }

    public static PostProcessorProvider getInstance() {
        if (instance == null) {
            instance = new PostProcessorProvider();
        }
        return instance;
    }

    public <T> PostProcessor<T> getProcessor(Class<T> clz) {
        return (PostProcessor<T>) process.get(clz);
    }

    public <T> List<PostProcessor<? super T>> getProcessors(Class<T> componentClass) {
        List<PostProcessor<? super T>> postProcessors = Lists.newArrayList();
        for (Map.Entry<Class<?>, PostProcessor<?>> entry : process.entrySet()) {
            if (entry.getKey().isAssignableFrom(componentClass)) {
                PostProcessor<? super T> postProcessor = (PostProcessor<? super T>) entry.getValue();
                postProcessors.add(postProcessor);
            }
        }
        return postProcessors;
    }

    public <T> void register(Class<T> componentClass, Class<? extends PostProcessor<? super T>> postProcessorClass) {
        PostProcessor<? super T> postProcessor = DependProvider.getInstance().newInstance(postProcessorClass);
        register(componentClass, postProcessor);
    }

    public <T> void register(Class<T> clazz, PostProcessor<? super T> postProcessor) {
        Method method = getOneMethod(postProcessor.getClass(), POSTPROCESS_NAME);
        Class<?>[] params = method.getParameterTypes();
        Precondition.checkArgument(params.length == 1, "校验异常：参数类型匹配不正确！");
        Precondition.checkArgument(params[0].isAssignableFrom(clazz), "校验异常：参数类型匹配不正确！");

        if (process.containsKey(clazz)) {
            PostProcessor<? super T> existProcessor = (PostProcessor<? super T>) process.get(clazz);
            if (existProcessor instanceof ComposPostProcessorImpl) {
                ((ComposPostProcessorImpl<T>) existProcessor).compose(postProcessor);
            } else {//合并
                process.remove(clazz);
                ComposPostProcessorImpl<T> compositeProcessor = new ComposPostProcessorImpl<T>(existProcessor);
                compositeProcessor.compose(postProcessor);
                process.put(clazz, compositeProcessor);
            }
        } else {
            process.put(clazz, postProcessor);
        }
    }

    public void unRegister(Class<?> componentClass) {
        process.remove(componentClass);
    }

    private Method getOneMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getMethods())
            if (m.getName().equals(name)) {
                return m;
            }
        return null;
    }

    public void clear() {
        process.clear();
    }

}
