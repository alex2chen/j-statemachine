package com.github.middleware.fsm.config.loader.support;

import com.github.middleware.fsm.builder.event.BaseEvent;
import com.github.middleware.fsm.builder.event.BaseListener;
import com.github.middleware.fsm.builder.event.EventType;
import com.github.middleware.fsm.builder.event.ListenerMapping;
import com.github.middleware.fsm.builder.event.annotation.ListenerOrder;
import com.github.middleware.fsm.builder.event.dto.ListenerMappingContext;
import com.github.middleware.fsm.builder.event.dto.ListenerMappingContextItem;
import com.github.middleware.fsm.builder.plugin.PluginProvider;
import com.github.middleware.fsm.config.loader.AnnotationResovler;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.github.middleware.fsm.config.annotation.State;
import com.github.middleware.fsm.config.annotation.States;
import com.github.middleware.fsm.config.annotation.Transition;
import com.github.middleware.fsm.config.annotation.Transitions;
import com.github.middleware.fsm.core.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class AnnotationResovlerImpl implements AnnotationResovler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationResovlerImpl.class);
    private List<State> declareStates;
    private List<Transition> declareTransitions;

    public AnnotationResovlerImpl() {
        declareStates = Lists.newArrayList();
        declareTransitions = Lists.newArrayList();
    }

    @Override
    public List<State> processStateLoader(final Class<?> stateMachineClz) {
        ReflectUtil.processStateMachineClass(stateMachineClz, new DeclareStateFunction());
        return declareStates;
    }

    @Override
    public List<Transition> processTransitionLoader(final Class<?> stateMachineClz) {
        ReflectUtil.processStateMachineClass(stateMachineClz, new DeclareTransitionFunction());
        return declareTransitions;
    }

    protected void addDeclareState(State declareState) {
        if (declareState != null) this.declareStates.add(declareState);
    }

    protected void addDeclareTransition(Transition declareTransition) {
        if (declareTransition != null) this.declareTransitions.add(declareTransition);
    }

    @Override
    public ListenerMappingContext processEventListener(final PluginProvider providerListener) {
        List<String> visitMethods = Lists.newArrayList();
        String methodSign = null;
        Method[] methods = providerListener.getClass().getDeclaredMethods();
        sortListenerOrder(methods);
        ListenerMappingContext mappingContext = new ListenerMappingContext(providerListener);
        ListenerMappingContextItem mappingContextItem = null;
        for (Method listenerMethod : methods) {
            methodSign = listenerMethod.toString();
            if (visitMethods.contains(methodSign)) continue;
            visitMethods.add(methodSign);
            for (int i = 0; i < ListenerMapping.SATE_LISTENER_MAPPING.length; ++i) {
                mappingContextItem = resovlerMethodAnotation(EventType.Transition, listenerMethod, (Class<? extends Annotation>) ListenerMapping.SATE_LISTENER_MAPPING[i][0],
                        (Class<? extends BaseListener>) ListenerMapping.SATE_LISTENER_MAPPING[i][1], (Class<? extends BaseEvent>) ListenerMapping.SATE_LISTENER_MAPPING[i][2]);
                //一个方法暂不能监听多个类型
                if (mappingContextItem != null) break;
            }
            if (mappingContextItem == null) {
                for (int i = 0; i < ListenerMapping.ACTION_LISTENR_MAPPING.length; ++i) {
                    mappingContextItem = resovlerMethodAnotation(EventType.Action, listenerMethod, (Class<? extends Annotation>) ListenerMapping.ACTION_LISTENR_MAPPING[i][0],
                            (Class<? extends BaseListener>) ListenerMapping.ACTION_LISTENR_MAPPING[i][1], (Class<? extends BaseEvent>) ListenerMapping.ACTION_LISTENR_MAPPING[i][2]);
                    //一个方法暂不能监听多个类型
                    if (mappingContextItem != null) break;
                }
            }
            if (mappingContextItem != null) mappingContext.addMappingContextItem(mappingContextItem);
        }
        return mappingContext;
    }

    private ListenerMappingContextItem resovlerMethodAnotation(EventType eventType, Method method, Class<? extends Annotation> listenerAn, Class<? extends BaseListener> listenerCls, Class<? extends BaseEvent> listenerEvent) {
        ListenerMappingContextItem mappingContextItem = null;
        Annotation methodAn = method.getAnnotation(listenerAn);
        if (methodAn != null) {
            Method whenMethod = ReflectUtil.getMethod(methodAn.getClass(), ListenerMapping.LISTENER_ANNOTATION_WHEN_NAME, new Class[0]);
            String when = whenMethod == null ? StringUtils.EMPTY : (String) ReflectUtil.invoke(whenMethod, methodAn);
            Field methodField = ReflectUtil.getField(listenerCls, ListenerMapping.LISTENER_FIELD_NAME);
            if (methodField != null && Modifier.isStatic(methodField.getModifiers())) {
                Method eventMethod = (Method) ReflectUtil.getStatic(methodField);
                mappingContextItem = new ListenerMappingContextItem(listenerAn, listenerCls, listenerEvent);
                mappingContextItem.setListenrMethod(eventMethod);
                mappingContextItem.setFirePluginMethod(method);
                mappingContextItem.setWhen(when);
                mappingContextItem.setEventType(eventType);
            } else {
                LOGGER.warn("不能在" + listenerCls + "中找到方法METHOD.");
            }
        }
        return mappingContextItem;
    }

    @Deprecated
    private void sortListenerOrder(Method[] methods) {
        Arrays.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                ListenerOrder or1 = o1.getAnnotation(ListenerOrder.class);
                ListenerOrder or2 = o2.getAnnotation(ListenerOrder.class);
                if (or1 != null && or2 != null) {
                    return or1.value() - or2.value();
                } else if (or1 != null && or2 == null) {
                    return -1;
                } else if (or1 == null && or2 != null) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    /**
     * 处理state注解类
     */
    private class DeclareStateFunction implements Function<Class<?>, Boolean> {
        @Override
        public Boolean apply(Class<?> k) {
            //支持直接注解
            addDeclareState(k.getAnnotation(State.class));
            //也支持嵌套
            States states = k.getAnnotation(States.class);
            if (states != null && states.value() != null) {
                for (State s : states.value()) {
                    AnnotationResovlerImpl.this.addDeclareState(s);
                }
            }
            return true;
        }
    }

    /**
     * 处理Transition注解类
     */
    private class DeclareTransitionFunction implements Function<Class<?>, Boolean> {
        @Override
        public Boolean apply(Class<?> k) {
            addDeclareTransition(k.getAnnotation(Transition.class));
            Transitions transitions = k.getAnnotation(Transitions.class);
            if (transitions != null && transitions.value() != null) {
                for (Transition t : transitions.value()) {
                    AnnotationResovlerImpl.this.addDeclareTransition(t);
                }
            }
            return true;
        }
    }
}
