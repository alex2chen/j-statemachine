package com.github.middleware.fsm.builder;

import com.github.middleware.fsm.builder.chain.LocalTransitionBuilder;
import com.github.middleware.fsm.builder.chain.TransitionDefine;
import com.github.middleware.fsm.builder.chain.support.TransitionImpl;
import com.github.middleware.fsm.builder.exec.dto.RuleExecContext;
import com.github.middleware.fsm.builder.exec.support.MethodCallImpl;
import com.github.middleware.fsm.builder.exec.support.MethodCallProxyImpl;
import com.github.middleware.fsm.builder.plugin.PluginBuilder;
import com.github.middleware.fsm.builder.plugin.PluginProvider;
import com.github.middleware.fsm.builder.state.StateDefine;
import com.github.middleware.fsm.builder.state.support.StateImpl;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.builder.exec.ActionExecService;
import com.github.middleware.fsm.builder.exec.EntryExitBuilder;
import com.github.middleware.fsm.builder.machine.dto.StateMachineData;
import com.github.middleware.fsm.config.application.StateMachineConfig;
import com.github.middleware.fsm.core.el.MvelScriptManager;
import com.github.middleware.fsm.filter.validator.PreChecker;
import com.github.middleware.fsm.core.factory.DependProvider;
import com.github.middleware.fsm.filter.validator.support.MvelCheckerImpl;
import com.github.middleware.fsm.core.types.TypeReference;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 快捷入口
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class StateMachines {
    /**
     * 构建Local Transition Builder
     *
     * @param states
     * @param execContext
     * @param <T>
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <T extends StateMachine<T, S, E, C>, S, E, C> LocalTransitionBuilder<T, S, E, C> newLocalTransitionBuilder(
            Map<S, StateDefine<T, S, E, C>> states, RuleExecContext execContext) {
        return DependProvider.getInstance().newInstance(new TypeReference<LocalTransitionBuilder<T, S, E, C>>() {
        }, new Class[]{Map.class, RuleExecContext.class}, new Object[]{states, execContext});
    }

    /**
     * 获取状态项
     *
     * @param states
     * @param stateId
     * @param <T>
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <T extends StateMachine<T, S, E, C>, S, E, C> StateDefine<T, S, E, C> getState(
            Map<S, StateDefine<T, S, E, C>> states, S stateId) {
        StateDefine<T, S, E, C> state = states.get(stateId);
        if (state == null) {
            state = newState(stateId);
            states.put(stateId, state);
        }
        return state;
    }

    /**
     * 新建状态项
     *
     * @param stateId
     * @param <T>
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <T extends StateMachine<T, S, E, C>, S, E, C> StateDefine<T, S, E, C> newState(S stateId) {
        return DependProvider.getInstance().newInstance(new TypeReference<StateImpl<T, S, E, C>>() {
                                                        },
                new Class[]{Object.class}, new Object[]{stateId});
    }

    /**
     * 实例化CallActionProxy
     *
     * @param methodName
     * @param executionContext
     * @param <T>
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <T extends StateMachine<T, S, E, C>, S, E, C> Action<T, S, E, C> newMethodCallActionProxy(
            String methodName, RuleExecContext executionContext) {
        return DependProvider.getInstance().newInstance(new TypeReference<MethodCallProxyImpl<T, S, E, C>>() {
                                                        },
                new Class[]{String.class, RuleExecContext.class}, new Object[]{methodName, executionContext});
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> Action<T, S, E, C> newMethodCallAction(
            Method method, int weight, RuleExecContext execContext) {
        return DependProvider.getInstance().newInstance(new TypeReference<MethodCallImpl<T, S, E, C>>() {
                                                        },
                new Class[]{Method.class, int.class, RuleExecContext.class}, new Object[]{method, weight, execContext});
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> ActionExecService<T, S, E, C> newActionExecService(StateMachineConfig config) {
        return DependProvider.getInstance().newInstance(new TypeReference<ActionExecService<T, S, E, C>>() {
        }, new Class[]{StateMachineConfig.class}, new Object[]{config});
    }

    /**
     * 实例化EntryExitActionBuilder
     *
     * @param state
     * @param isEntryAction
     * @param executionContext
     * @param <T>
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <T extends StateMachine<T, S, E, C>, S, E, C> EntryExitBuilder<T, S, E, C> newEntryExitActionBuilder(
            StateDefine<T, S, E, C> state, boolean isEntryAction, RuleExecContext executionContext) {
        return DependProvider.getInstance().newInstance(new TypeReference<EntryExitBuilder<T, S, E, C>>() {
                                                        },
                new Class[]{StateDefine.class, boolean.class, RuleExecContext.class},
                new Object[]{state, isEntryAction, executionContext});
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> PluginBuilder<T, S, E, C> newPluginBuilder(PluginProvider pluginProvider, StateMachineData<T, S, E, C> data) {
        return DependProvider.getInstance().newInstance(new TypeReference<PluginBuilder>() {
        }, new Class[]{PluginProvider.class, StateMachineData.class}, new Object[]{pluginProvider, data});
    }

    /**
     * 新建mv el
     *
     * @param expression
     * @param scriptManager
     * @param <C>
     * @return
     */
    public static <C> PreChecker<C> newMvelCondition(String expression, MvelScriptManager scriptManager) {
        return DependProvider.getInstance().newInstance(new TypeReference<MvelCheckerImpl<C>>() {
                                                        },
                new Class<?>[]{String.class, MvelScriptManager.class}, new Object[]{expression, scriptManager});
    }

    /**
     * 构建transition
     *
     * @param <T>
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <T extends StateMachine<T, S, E, C>, S, E, C> TransitionDefine<T, S, E, C> newTransition() {
        return DependProvider.getInstance().newInstance(new TypeReference<TransitionImpl<T, S, E, C>>() {
        });
    }
}
