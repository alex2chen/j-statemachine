package com.github.middleware.fsm.builder.plugin.support;

import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.StateMachines;
import com.github.middleware.fsm.builder.event.*;
import com.github.middleware.fsm.builder.event.annotation.AsyncExec;
import com.github.middleware.fsm.builder.event.dto.ListenerMappingContext;
import com.github.middleware.fsm.builder.event.dto.ListenerMappingContextItem;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.builder.plugin.PluginBuilder;
import com.github.middleware.fsm.builder.plugin.PluginProvider;
import com.github.middleware.fsm.config.loader.AnnotationResovler;
import com.github.middleware.fsm.core.factory.DependProvider;
import com.github.middleware.fsm.core.utils.Precondition;
import com.github.middleware.fsm.core.utils.ReflectUtil;
import com.github.middleware.fsm.exception.StateMachineException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.github.middleware.fsm.builder.machine.dto.StateMachineData;
import com.github.middleware.fsm.builder.machine.support.AbstractStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;


/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/17
 */
public class PluginBuilderImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements PluginBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginBuilderImpl.class);
    //private LinkedListMultimap<String, List<ListenerMappingContext>> plugins;
    private PluginProvider pluginProvider;
    private StateMachineData<T, S, E, C> data;
    private AnnotationResovler annotationResovler;
    private ListenerMappingContext mappingContext;
    private boolean isInited = false;

    public PluginBuilderImpl(PluginProvider pluginProvider, StateMachineData<T, S, E, C> data) {
        annotationResovler = DependProvider.getInstance().newInstance(AnnotationResovler.class);
        this.pluginProvider = pluginProvider;
        this.data = data;
    }

    public void init() {
        Precondition.checkNotNull(pluginProvider, "校验异常：插件实例化不能为空.");
        Precondition.checkNotNull(data, "校验异常：状态机构造器初始化数据为空.");
        mappingContext = annotationResovler.processEventListener(pluginProvider);
        if (mappingContext.getMappingContextItems().isEmpty()) return;
        Object proxylistenerMethod = null;
        for (ListenerMappingContextItem item : mappingContext.getMappingContextItems()) {
            proxylistenerMethod = newProxylistenerMethod(mappingContext.getPluginProvider(), item.getFirePluginMethod(), item.getListenerClz(), item.getListenrMethod(), item.getWhen());
            item.setProxylistenerMethod(proxylistenerMethod);
        }
        LOGGER.debug("fsm.load 读取插件信息完成.");
        isInited = true;
    }

    private boolean checkOk() {
        if (isInited == false || mappingContext.getMappingContextItems().isEmpty()) return false;
        return true;
    }

    @Override
    public void install() {
        if (isInited == false) init();
        if (data.getPlugins().contains(mappingContext)) {
            LOGGER.warn("fsm.load 插件添加失败，添加了一个重复的插件！");
            return;
        }
        if (checkOk()) {
            data.addPlugin(mappingContext);
            LOGGER.debug("fsm.load 插件安装完成,待状态机启动生效.");
        }
    }

    @Override
    public void unInstall() {
        if (isInited == false) init();
        if (data.getPlugins().contains(mappingContext)) {
            data.getPlugins().remove(mappingContext);
        }
        LOGGER.debug("fsm.load 插件卸载完成,待状态机重启生效.");
    }

    private Object newProxylistenerMethod(PluginProvider pluginProvider, Method firePluginMethod, Class<? extends BaseListener> listenerClz, Method listenrMethod, String when) {
        final String listenerMethodName = listenrMethod.getName();
        AsyncExec asyncAnnotation = firePluginMethod.getAnnotation(AsyncExec.class);
        final boolean isAsync = asyncAnnotation != null;
        final long timeout = asyncAnnotation != null ? asyncAnnotation.timeout() : -1;
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                if (methodName.equals(listenerMethodName)) {
                    if (args[0] instanceof TransitionEvent) {
                        TransitionEvent<T, S, E, C> event = (TransitionEvent<T, S, E, C>) args[0];
                        return invokeTransitionListenerMethod(pluginProvider, firePluginMethod, when, event);
                    } else if (args[0] instanceof ActionEvent) {
                        ActionEvent<T, S, E, C> event = (ActionEvent<T, S, E, C>) args[0];
                        return invokeActionListenerMethod(pluginProvider, firePluginMethod, when, event);
                    } else {
                        throw new StateMachineException("未知参数类型 " + args[0].getClass().getName() + "方法代理失败.");
                    }
                } else if (methodName.equals("timeout") && isAsync) {
                    return timeout;
                }
                throw new StateMachineException("调用方法 " + method.getName() + "失败.");
            }
        };

        Class<?>[] implementedInterfaces = isAsync ? new Class<?>[]{listenerClz, AsyncEventListener.class} :
                new Class<?>[]{listenerClz};
        Object proxyListener = Proxy.newProxyInstance(StateMachine.class.getClassLoader(), implementedInterfaces, invocationHandler);
        return proxyListener;
    }

    private Object invokeActionListenerMethod(PluginProvider pluginProvider, Method firePluginMethod, String when, ActionEvent<T, S, E, C> event) {
        boolean isPass = true;
        if (!Strings.isNullOrEmpty(when))
            isPass = StateMachines.newMvelCondition(when, data.getScriptManager()).validate(event.getContext());
        if (!isPass) return null;
        Class<?>[] parameterTypes = firePluginMethod.getParameterTypes();
        if (parameterTypes.length == 0) {
            return ReflectUtil.invoke(firePluginMethod, pluginProvider);
        }
        List<Object> parameterValues = Lists.newArrayList();
        boolean isSourceStateSet = false, isTargetStateSet = false, isEventSet = false, isContextSet = false;
        for (Class<?> parameterType : parameterTypes) {
            if (!isSourceStateSet && parameterType.isAssignableFrom(data.getStateClz())) {
                parameterValues.add(event.getFrom());
                isSourceStateSet = true;
            } else if (!isTargetStateSet && parameterType.isAssignableFrom(data.getStateClz())) {
                parameterValues.add(event.getTo());
                isTargetStateSet = true;
            } else if (!isEventSet && parameterType.isAssignableFrom(data.getEventClz())) {
                parameterValues.add(event.getEvent());
                isEventSet = true;
            } else if (!isContextSet && parameterType.isAssignableFrom(data.getContextClz())) {
                parameterValues.add(event.getContext());
                isContextSet = true;
            } else if (parameterType.isAssignableFrom(AbstractStateMachine.class)) {
                parameterValues.add(event.getStateMachine());
            } else if (parameterType.isAssignableFrom(Action.class)) {
                parameterValues.add(event.getExecutionTarget());
            } else if (parameterType == int[].class) {
                parameterValues.add(event.getMOfN());
            } else if (event instanceof ExecActionExceptionEvent && parameterType.isAssignableFrom(StateMachineException.class)) {
                parameterValues.add(((ExecActionExceptionEvent<T, S, E, C>) event).getException());
            } else {
                parameterValues.add(null);
            }
        }
        return ReflectUtil.invoke(firePluginMethod, pluginProvider, parameterValues.toArray());
    }

    private Object invokeTransitionListenerMethod(PluginProvider pluginProvider, Method firePluginMethod, String when, TransitionEvent<T, S, E, C> event) {
        boolean isPass = true;
        if (!Strings.isNullOrEmpty(when))
            isPass = StateMachines.newMvelCondition(when, data.getScriptManager()).validate(event.getContext());
        if (!isPass) return null;
        Class<?>[] parameterTypes = firePluginMethod.getParameterTypes();
        if (parameterTypes.length == 0) {
            return ReflectUtil.invoke(firePluginMethod, pluginProvider);
        }
        List<Object> parameterValues = Lists.newArrayList();
        boolean isSourceStateSet = false, isTargetStateSet = false, isEventSet = false, isContextSet = false;
        for (Class<?> parameterType : parameterTypes) {
            if (!isSourceStateSet && parameterType.isAssignableFrom(data.getStateClz())) {
                parameterValues.add(event.getSourceState());
                isSourceStateSet = true;
            } else if (!isTargetStateSet && event instanceof TransitionEndEvent &&
                    parameterType.isAssignableFrom(data.getStateClz())) {
                parameterValues.add(((TransitionEndEvent<T, S, E, C>) event).getTargetState());
                isTargetStateSet = true;
            } else if (!isTargetStateSet && event instanceof TransitionCompleteEvent &&
                    parameterType.isAssignableFrom(data.getStateClz())) {
                parameterValues.add(((TransitionCompleteEvent<T, S, E, C>) event).getTargetState());
                isTargetStateSet = true;
            } else if (!isTargetStateSet && event instanceof TransitionExceptionEvent &&
                    parameterType.isAssignableFrom(data.getStateClz())) {
                parameterValues.add(((TransitionExceptionEvent<T, S, E, C>) event).getTargetState());
                isTargetStateSet = true;
            } else if (!isEventSet && parameterType.isAssignableFrom(data.getEventClz())) {
                parameterValues.add(event.getCause());
                isEventSet = true;
            } else if (!isContextSet && parameterType.isAssignableFrom(data.getContextClz())) {
                parameterValues.add(event.getContext());
                isContextSet = true;
            } else if (parameterType.isAssignableFrom(AbstractStateMachine.class)) {
                parameterValues.add(event.getStateMachine());
            } else if (event instanceof TransitionExceptionEvent &&
                    parameterType.isAssignableFrom(StateMachineException.class)) {
                parameterValues.add(((TransitionExceptionEvent<T, S, E, C>) event).getException());
            } else {
                parameterValues.add(null);
            }
        }
        return ReflectUtil.invoke(firePluginMethod, pluginProvider, parameterValues.toArray());
    }
}
