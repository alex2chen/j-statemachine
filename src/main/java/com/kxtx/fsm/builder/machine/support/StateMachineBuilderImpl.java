package com.kxtx.fsm.builder.machine.support;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.StateMachines;
import com.kxtx.fsm.builder.chain.*;
import com.kxtx.fsm.builder.exec.Action;
import com.kxtx.fsm.builder.exec.EntryExitBuilder;
import com.kxtx.fsm.builder.machine.annotation.HoldPayload;
import com.kxtx.fsm.builder.exec.dto.RuleExecContext;
import com.kxtx.fsm.builder.exec.dto.RegularTransition;
import com.kxtx.fsm.builder.exec.support.AbstractAction;
import com.kxtx.fsm.builder.exec.support.ActionWrapper;
import com.kxtx.fsm.builder.machine.StateMachineBuilder;
import com.kxtx.fsm.builder.plugin.PluginBuilder;
import com.kxtx.fsm.builder.plugin.PluginProvider;
import com.kxtx.fsm.filter.annotation.PostConstruct;
import com.kxtx.fsm.builder.machine.dto.StateMachineData;
import com.kxtx.fsm.builder.machine.support.visit.StateMachineProxy;
import com.kxtx.fsm.config.annotation.*;
import com.kxtx.fsm.config.loader.AnnotationResovler;
import com.kxtx.fsm.core.convert.ConverterProvider;
import com.kxtx.fsm.core.el.MvelScriptManager;
import com.kxtx.fsm.config.application.StateMachineConfig;
import com.kxtx.fsm.core.convert.Converter;
import com.kxtx.fsm.core.utils.Precondition;
import com.kxtx.fsm.exception.NotSupportedException;
import com.kxtx.fsm.exception.SateAlreadyExistsException;
import com.kxtx.fsm.exception.StateNotDefinedException;
import com.kxtx.fsm.filter.validator.PreChecker;
import com.kxtx.fsm.builder.state.support.visit.ProxyStateDefine;
import com.kxtx.fsm.builder.state.StateDefine;
import com.kxtx.fsm.exception.StateMachineException;
import com.kxtx.fsm.filter.validator.support.PreCheckers;
import com.kxtx.fsm.core.factory.DependProvider;
import com.kxtx.fsm.filter.wrapper.PostProcessor;
import com.kxtx.fsm.filter.wrapper.PostProcessorProvider;
import com.kxtx.fsm.core.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/4
 */
public class StateMachineBuilderImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements StateMachineBuilder<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineBuilderImpl.class);
    private Map<String, String> stateAlias;
    private Map<S, StateDefine<T, S, E, C>> states;
    private StateMachineConfig defaultConfig;
    private StateMachineData<T, S, E, C> data;
    private Converter<S> stateConverter;
    private Converter<E> eventConverter;
    private AnnotationResovler annotationResovler;

    private Method postConstructMethod;
    private Constructor<? extends T> constructor;
    private Class<?>[] methodCallParamTypes;
    private boolean isInited = false;
    private boolean isLocalTransition = false;
    private RuleExecContext execContext;
    private List<RegularTransition<T, S, E, C>> actionInfoList;


    public StateMachineBuilderImpl(Class<? extends T> stateMachineClz, Class<S> stateClz, Class<E> eventClz, Class<C> contextClz, Class<?>... extraParamTypes) {
        Precondition.checkState(ReflectUtil.isInstanceType(stateMachineClz), "校验异常：状态机实现类不能被实例化!");
        Precondition.checkState(ReflectUtil.isStateMachineType(stateMachineClz), "校验异常：状态机实现类必须继承AbstractStateMachine!");
        states = Maps.newConcurrentMap();
        stateAlias = Maps.newHashMap();
        actionInfoList = Lists.newArrayList();
        defaultConfig = new StateMachineConfig();
        data = new StateMachineData(states, stateMachineClz);

        initGenericsField(stateClz, eventClz, contextClz, extraParamTypes);
        initDeclareComponent();
    }

    /**
     * 初始化变量
     *
     * @param stateClz
     * @param eventClz
     * @param contextClz
     * @param extraParamTypes
     */
    private void initGenericsField(Class<S> stateClz, Class<E> eventClz, Class<C> contextClz, Class<?>... extraParamTypes) {
        StateMachineParameters genericsParameters = ReflectUtil.findAnnotation(data.getStateMachineClz(), StateMachineParameters.class);
        if (stateClz == Object.class && genericsParameters != null) {
            data.setStateClz((Class<S>) genericsParameters.stateType());
        } else {
            data.setStateClz(stateClz);
        }
        if (eventClz == Object.class && genericsParameters != null) {
            data.setEventClz((Class<E>) genericsParameters.eventType());
        } else {
            data.setEventClz(eventClz);
        }
        if (contextClz == Object.class && genericsParameters != null) {
            data.setContextClz((Class<C>) genericsParameters.contextType());
        } else {
            data.setContextClz(contextClz);
        }
        data.setExtraParamTypes(extraParamTypes != null ? extraParamTypes : new Class<?>[0]);

        boolean hasHoldExecContext = ReflectUtil.findAnnotation(data.getStateMachineClz(), HoldPayload.class) != null;
        stateConverter = ConverterProvider.INSTANCE.getConverter(data.getStateClz());
        eventConverter = ConverterProvider.INSTANCE.getConverter(data.getEventClz());
        annotationResovler = DependProvider.getInstance().newInstance(AnnotationResovler.class);
        data.setScriptManager(DependProvider.getInstance().newInstance(MvelScriptManager.class));
        methodCallParamTypes = hasHoldExecContext ? new Class<?>[]{data.getStateClz(), data.getStateClz(), data.getEventClz()} :
                new Class<?>[]{data.getStateClz(), data.getStateClz(), data.getEventClz(), data.getContextClz()};
        this.execContext = new RuleExecContext(data.getScriptManager(), data.getStateMachineClz(), methodCallParamTypes);
        List<Method> methods = ReflectUtil.getAnnotatedMethods(data.getStateMachineClz(), PostConstruct.class, data.getExtraParamTypes());
        if (!methods.isEmpty()) {
            if (methods.size() != 1) LOGGER.warn("fsm.load 状态机实现类实现了多个PostConstruct可能不会都被执行.");
            postConstructMethod = methods.get(0);
        }
    }

    @Override
    public LocalTransitionBuilder<T, S, E, C> localTransition() {
        checkInit();
        isLocalTransition = true;
        return StateMachines.newLocalTransitionBuilder(states, execContext);
    }

    @Override
    public T newStateMachine() {
        return newStateMachine(null, new Object[0]);
    }

    @Override
    public T newStateMachine(S initialStateName) {
        return newStateMachine(initialStateName, new Object[0]);
    }

    @Override
    public T newStateMachine(S initialStateName, Object... extraParams) {
        return newStateMachine(null, initialStateName, defaultConfig, extraParams);
    }

    @Override
    public T newStateMachine(T fsmBean, S initialStateName, StateMachineConfig configuration, Object... extraParams) {
        if (!isLocalTransition) localTransition();
        //if (!isInited) initDeclareComponent();
        isInited = true;
        boolean hasInitstate = initialStateName != null && states.get(initialStateName) == null;
        boolean hasNotInitstate = initialStateName == null && data.initialState() == null;
        if (hasInitstate || hasNotInitstate) {
            throw new StateNotDefinedException("校验异常：初始化状态未正确定义！");
        }
        try {
            constructor = ReflectUtil.getConstructor(data.getStateMachineClz(), data.getExtraParamTypes());
        } catch (Exception e1) {
            try {
                constructor = ReflectUtil.getConstructor(data.getStateMachineClz(), new Class<?>[0]);
            } catch (Exception e2) {
                throw new StateMachineException("校验异常：不能获取constructor " + data.getStateMachineClz().getName());
            }
        }
        Class<?>[] constParamTypes = constructor.getParameterTypes();
        T stateMachine;
        if (constParamTypes == null || constParamTypes.length == 0) {
            stateMachine = ReflectUtil.newInstance(constructor);
        } else {
            stateMachine = ReflectUtil.newInstance(constructor, extraParams);
        }
        if (fsmBean != null) {
            stateMachine = fsmBean;
        }
        AbstractStateMachine<T, S, E, C> stateMachineImpl = (AbstractStateMachine<T, S, E, C>) stateMachine;
        if (configuration != null) data.identifier(configuration.getIdentifier().get());
        stateMachineImpl.prePostConstruct(initialStateName, states, configuration, data);
        if (postConstructMethod != null && data.getExtraParamTypes().length == extraParams.length) {
            try {
                ReflectUtil.invoke(postConstructMethod, stateMachine, extraParams);
            } catch (StateMachineException e) {
                throw new StateMachineException("操作异常：调用postConstruct出现错误>" + e.getTargetException());
            }
        }
        postProcessStateMachine((Class<T>) data.getStateMachineClz(), stateMachine);

        return stateMachine;
    }

    /**
     * 后置处理
     *
     * @param clz
     * @param component
     * @return
     */
    private T postProcessStateMachine(Class<T> clz, T component) {
        if (component != null) {
            List<PostProcessor<? super T>> postProcessors = PostProcessorProvider.getInstance().getProcessors(clz);
            for (PostProcessor<? super T> postProcessor : postProcessors) {
                postProcessor.postProcess(component);
            }
        }
        return component;
    }

    /**
     * 初始化声明的组件信息
     */
    private void initDeclareComponent() {
        if (isInited) return;
        Precondition.checkState(annotationResovler != null, "校验异常：annotationResovler is null");
        Precondition.checkState(stateConverter != null, "校验异常：state converter未注册！");
        Precondition.checkState(eventConverter != null, "校验异常：event converter未注册！");
        List<State> declareStates = annotationResovler.processStateLoader(data.getStateMachineClz());
        for (State item : declareStates) {
            if (item != null) buildDeclareState(item);
        }
        List<Transition> declareTransitions = annotationResovler.processTransitionLoader(data.getStateMachineClz());
        for (Transition item : declareTransitions) {
            if (item != null) buildDeclareTransition(item);
        }
        //installExtensionMethods();
        disableEndStateActions();
        checkStateMachineDefine();
        swithProxyStateMachine();
        //isInited = true;
    }

    /**
     * 验证state、transition的初始化信息
     */
    private void checkStateMachineDefine() {
        for (StateDefine<T, S, E, C> state : states.values()) {
            state.check();
        }
    }

    /**
     * 安装state、transition的默认拓展方法
     */
    @Deprecated
    private void installExtensionMethods() {
        for (StateDefine<T, S, E, C> state : states.values()) {
            if (state.isEndState()) continue;
            String[] exitMethods = getStateEntryExitMethods(state, false);
            for (String exitMethod : exitMethods) {
                addStateEntryExitMethodAction(exitMethod, methodCallParamTypes, state, false);
            }
            for (TransitionDefine<T, S, E, C> transition : state.getTransitions()) {
                String[] transitionMethods = getTransitionMethods(transition);
                for (String transitionMethod : transitionMethods) {
                    addTransitionMethodAction(transitionMethod, methodCallParamTypes, transition);
                }
            }
            String[] entryMethods = getStateEntryExitMethods(state, true);
            for (String entryMethod : entryMethods) {
                addStateEntryExitMethodAction(entryMethod, methodCallParamTypes, state, true);
            }
        }
    }

    /**
     * 获取State EntryExitMethods
     * beforeExitAny > exit'S' > afterExitAny
     * beforeEntryAny > entry'S' > afterEntryAny
     */
    private String[] getStateEntryExitMethods(StateDefine<T, S, E, C> state, boolean isEntry) {
        String prefix = (isEntry ? "entry" : "exit");
        String postfix = (isEntry ? "EntryAny" : "ExitAny");
        return new String[]{
                "before" + postfix,
                prefix + (!state.isEndState() ? stateConverter.objToString(state.getName()) : StringUtils.capitalize(state.toString())),
                "after" + postfix
        };
    }

    /**
     * 获取transition Methods
     * eq:from:A ,to:B,on:A2B, When:check
     * transitFromAToBOnA2BWhencheck
     * transitFromAToBOnA2B
     * transitFromAnyToBOnA2B
     * transitFromAToAnyOnA2B
     * transitFromAToB
     * onA2B
     *
     * @param transition
     * @return
     */
    private String[] getTransitionMethods(TransitionDefine<T, S, E, C> transition) {
        StateDefine<T, S, E, C> fromState = transition.getFromState();
        StateDefine<T, S, E, C> toState = transition.getToState();
        E event = transition.getEvent();
        String fromStateName = stateConverter.objToString(fromState.getName());
        String toStateName = !toState.isEndState() ? stateConverter.objToString(toState.getName()) : StringUtils.capitalize(toState.toString());
        String eventName = eventConverter.objToString(event);
        String checkerName = transition.getPreChecker() == null ? "" : transition.getPreChecker().getName();
        return new String[]{
                "transitFrom" + fromStateName + "To" + toStateName + "On" + eventName + "When" + checkerName,
                "transitFrom" + fromStateName + "To" + toStateName + "On" + eventName,
                "transitFromAnyTo" + toStateName + "On" + eventName,
                "transitFrom" + fromStateName + "ToAnyOn" + eventName,
                "transitFrom" + fromStateName + "To" + toStateName,
                "on" + eventName
        };
    }

    /**
     * state中添加Entry Exit Method
     *
     * @param methodName
     * @param parameterTypes
     * @param stateDefine
     * @param isEntryAction
     */
    private void addStateEntryExitMethodAction(String methodName, Class<?>[] parameterTypes, StateDefine<T, S, E, C> stateDefine, boolean isEntryAction) {
        Method method = ReflectUtil.searchMethod(data.getStateMachineClz(), AbstractStateMachine.class, methodName, parameterTypes);
        if (method != null) {
            int weight = Action.WEIGHT_NORMAL;
            if (methodName.startsWith("before")) {
                weight = Action.WEIGHT_BEFORE;
            } else if (methodName.startsWith("after")) {
                weight = Action.WEIGHT_AFTER;
            }
            Action<T, S, E, C> methodCallAction = StateMachines.newMethodCallAction(method, weight, execContext);
            if (isEntryAction) {
                stateDefine.addEntryAction(methodCallAction);
            } else {
                stateDefine.addExitAction(methodCallAction);
            }
        }
    }

    /**
     * transition中添加Transition Methods
     *
     * @param methodName
     * @param parameterTypes
     * @param transition
     */
    private void addTransitionMethodAction(String methodName, Class<?>[] parameterTypes, TransitionDefine<T, S, E, C> transition) {
        Method method = ReflectUtil.searchMethod(data.getStateMachineClz(), AbstractStateMachine.class, methodName, parameterTypes);
        if (method != null) {
            Action<T, S, E, C> methodCallAction = StateMachines.newMethodCallAction(method, Action.WEIGHT_IGNORE, execContext);
            transition.addAction(methodCallAction);
        }
    }

    /**
     * 禁用end state的行为
     */
    private void disableEndStateActions() {
        for (StateDefine<T, S, E, C> state : states.values()) {
            if (!state.isEndState()) continue;
            state.addExitAction(new AbstractAction<T, S, E, C>() {
                @Override
                public void execute(S from, S to, E event, C context, T stateMachine, String actionName) {
                    throw new StateMachineException("校验异常：终结状态不填添加任何行为.");
                }

                @Override
                public String getName() {
                    return "_End_STATE_ACTION";
                }
            });

        }
    }

    /**
     * 动态代理ProxyStateMachine的处理
     */
    private void swithProxyStateMachine() {
        if (StateMachineProxy.class.isAssignableFrom(data.getStateMachineClz())) {
            Map<S, StateDefine<T, S, E, C>> proxyStates = Maps.newHashMap();
            for (StateDefine<T, S, E, C> state : states.values()) {
                ProxyStateDefine proxyState = (ProxyStateDefine) Proxy.newProxyInstance(
                        ProxyStateDefine.class.getClassLoader(),
                        new Class[]{ProxyStateDefine.class},
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args)
                                    throws Throwable {
                                if (method.getName().equals("getName")) {
                                    return state.getName();
                                } else if (method.getName().equals("getThis")) {
                                    return state.self();
                                } else if (method.getName().equals("equals")) {
                                    return state.equals(args[0]);
                                } else if (method.getName().equals("hashCode")) {
                                    return state.hashCode();
                                }
                                return method.invoke(state, args);
                            }
                        });
                proxyStates.put(state.getName(), StateDefine.class.cast(proxyState));
            }
            states.clear();
            states.putAll(proxyStates);
        }
    }


    /**
     * 检查状态机状态
     */
    private void checkInit() {
        if (isInited) {
            throw new StateMachineException("校验异常：状态机已经被初始化，无法修改其他信息");
        }
    }

    @Override
    public EntryExitBuilder<T, S, E, C> onEntry(S stateName) {
        checkInit();
        StateDefine<T, S, E, C> state = StateMachines.getState(states, stateName);
        return StateMachines.newEntryExitActionBuilder(state, true, execContext);
    }

    @Override
    public EntryExitBuilder<T, S, E, C> onExit(S stateName) {
        checkInit();
        StateDefine<T, S, E, C> state = StateMachines.getState(states, stateName);
        return StateMachines.newEntryExitActionBuilder(state, false, execContext);
    }

    @Override
    public PluginBuilder<T, S, E, C> newPluginBuilder(PluginProvider pluginProvider) {
        Precondition.checkNotNull(data, "校验失败：未完成Builder初始化");
        return StateMachines.newPluginBuilder(pluginProvider, data);
    }

    /**
     * build state
     *
     * @param state
     */
    private void buildDeclareState(State state) {
        S stateName = stateConverter.stringToObj(state.name());
        Precondition.checkNotNull(stateName, "校验异常：Cannot convert state of name " + stateName + ".");
        StateDefine<T, S, E, C> newState = defineState(stateName);
        newState.setEndState(state.isEnd());
        //处理状态继承关系
        if (!Strings.isNullOrEmpty(state.parent())) {
            S parentStateName = stateConverter.stringToObj(parseParentState(state.parent()));
            StateDefine<T, S, E, C> parentState = defineState(parentStateName);
            newState.setParentState(parentState);
            parentState.addChildState(newState);
            if (state.isInitial()) parentState.setInitialState(newState);
        }
        if (state.isInitial()) {
            data.initialState(stateName);
            data.currentState(stateName);
        }
        if (!Strings.isNullOrEmpty(state.entryMethod())) {
            Action<T, S, E, C> entryMethod = StateMachines.newMethodCallActionProxy(state.entryMethod(), execContext);
            onEntry(stateName).complete(entryMethod);
        }
        if (!Strings.isNullOrEmpty(state.exitMethod())) {
            Action<T, S, E, C> methodCallAction = StateMachines.newMethodCallActionProxy(state.exitMethod(), execContext);
            onExit(stateName).complete(methodCallAction);
        }
        if (!Strings.isNullOrEmpty(state.alias())) {
            if (stateAlias.containsKey(state.alias())) {
                throw new SateAlreadyExistsException("校验异常：Cannot define state alias>" + state.alias());
            } else {
                stateAlias.put(state.alias(), state.name());
            }
        }
    }

    /**
     * parent state转换
     *
     * @param value
     * @return
     */
    private String parseParentState(String value) {
        return (value != null && value.startsWith("#")) ? stateAlias.get(value.substring(1)) : value;
    }

    /**
     * 声明状态
     *
     * @param stateId
     * @return
     */
    private StateDefine<T, S, E, C> defineState(S stateId) {
        checkInit();
        return StateMachines.getState(states, stateId);
    }

    /**
     * build transition
     *
     * @param transition
     */
    private void buildDeclareTransition(Transition transition) {
        if ("*".equals(transition.from()) || "*".equals(transition.to()) || "*".equals(transition.on())) {
            buildRegularTransition(transition);
            throw new NotSupportedException("操作异常：暂未支持.");
            //return;
        }
        //when应该是具体类或静态内部类
        Precondition.checkArgument(ReflectUtil.isInstanceType(transition.whenPreChecker()), "校验异常：whenPreChecker type error!");
        Precondition.checkArgument(!transition.from().equals(transition.to()), "校验异常：transition from. to. same!");
        S fromState = stateConverter.stringToObj(parseParentState(transition.from()));
        Precondition.checkNotNull(fromState, "校验异常：Cannot convert state of name " + fromState + ".");
        S toState = stateConverter.stringToObj(parseParentState(transition.to()));
        Precondition.checkNotNull(toState, "校验异常：Cannot convert state of name " + toState + ".");
        E event = eventConverter.stringToObj(transition.on());
        Precondition.checkNotNull(event, "校验异常：Cannot convert event of name " + event + ".");
        if (states.get(fromState) != null) {
            StateDefine<T, S, E, C> fromStateObj = states.get(fromState);
            LOGGER.debug("fsm.load buildDeclareTransition get fromState:" + fromState + ",transitions:" + fromStateObj.getTransitions().size());
            for (TransitionDefine<T, S, E, C> t : fromStateObj.getTransitions()) {
                if (t.isEqual(fromState, toState, event, transition.whenPreChecker())) {
                    String callMethodName = transition.complete();
                    if (!Strings.isNullOrEmpty(callMethodName)) {
                        Action<T, S, E, C> callAction = StateMachines.newMethodCallActionProxy(callMethodName, execContext);
                        t.addAction(callAction);
                    }
                }
            }
        }
        LocalTransitionBuilder<T, S, E, C> transitionBuilder = StateMachines.newLocalTransitionBuilder(states, execContext);
        From<T, S, E, C> fromBuilder = transitionBuilder.from(fromState);
        boolean isTargetEnd = transition.isTargetEnd() || StateMachines.getState(states, toState).isEndState();
        To<T, S, E, C> toBuilder = isTargetEnd ? fromBuilder.toEnd(toState) : fromBuilder.to(toState);
        On<T, S, E, C> onBuilder = toBuilder.on(event);
        PreChecker<C> c = null;
        try {
            //when的二选一吧
            if (transition.whenPreChecker() != PreCheckers.Always.class) {
                Constructor<?> constructor = transition.whenPreChecker().getDeclaredConstructor();
                constructor.setAccessible(true);
                c = (PreChecker<C>) constructor.newInstance();
            } else if (StringUtils.isNotEmpty(transition.whenMVEL())) {
                c = StateMachines.newMvelCondition(transition.whenMVEL(), data.getScriptManager());
            }
        } catch (Exception e) {
            LOGGER.error("fsm.load buildDeclareTransition occure error:" + transition.whenPreChecker().getName() + ".");
            c = PreCheckers.never();
        }
        When<T, S, E, C> whenBuilder = c != null ? onBuilder.when(c) : onBuilder;
        if (!Strings.isNullOrEmpty(transition.complete())) {
            Action<T, S, E, C> methodCallAction = StateMachines.newMethodCallActionProxy(transition.complete(), execContext);
            whenBuilder.complete(methodCallAction);
        }
    }

    /**
     * build transition
     *
     * @param transition
     */
    @Deprecated
    private void buildRegularTransition(Transition transition) {
        S from = "*".equals(transition.from()) ? null : stateConverter.stringToObj(parseParentState(transition.from()));
        S to = "*".equals(transition.to()) ? null : stateConverter.stringToObj(parseParentState(transition.to()));
        E event = "*".equals(transition.on()) ? null : eventConverter.stringToObj(transition.on());
        RegularTransition<T, S, E, C> regularTransition = new RegularTransition<T, S, E, C>(from, to, event);
        if (!Strings.isNullOrEmpty(transition.complete())) {
            Action<T, S, E, C> action = StateMachines.newMethodCallActionProxy(transition.complete(), execContext);
            if (!transition.whenMVEL().isEmpty()) {
                PreChecker<C> condition = StateMachines.newMvelCondition(transition.whenMVEL(), data.getScriptManager());
                action = execPrecher(action, condition);
            }
            if (transition.whenPreChecker() != PreCheckers.Always.class) {
                PreChecker<C> condition = ReflectUtil.newInstance(transition.whenPreChecker());
                action = execPrecher(action, condition);
            }
            regularTransition.setActions(Collections.singletonList(action));
        }
        actionInfoList.add(regularTransition);
    }

    private Action<T, S, E, C> execPrecher(Action<T, S, E, C> action, PreChecker<C> checker) {
        return new ActionWrapper<T, S, E, C>(action) {
            @Override
            public void execute(S from, S to, E event, C context, T stateMachine, String actionName) {
                if (checker.validate(context)) {
                    super.execute(from, to, event, context, stateMachine, checker.getName());
                }
            }
        };
    }
}
