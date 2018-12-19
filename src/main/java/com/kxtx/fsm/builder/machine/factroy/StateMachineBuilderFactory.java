package com.kxtx.fsm.builder.machine.factroy;

import com.kxtx.fsm.builder.machine.StateMachineBuilder;
import com.kxtx.fsm.builder.machine.support.visit.StateMachineProxy;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.machine.support.visit.StateMachineBuilderProxy;
import com.kxtx.fsm.config.application.StateMachineConfig;
import com.kxtx.fsm.core.factory.DependProvider;
import com.kxtx.fsm.core.types.TypeReference;
import com.kxtx.fsm.exception.StateMachineException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public class StateMachineBuilderFactory {
    /**
     * 根据ProxyStateMachine来创建
     *
     * @param fsmClazz
     * @return
     */
    public static StateMachineBuilderProxy create(Class<? extends StateMachineProxy> fsmClazz) {
        return create(fsmClazz, new Class[0]);
    }

    /**
     * 根据ProxyStateMachine来创建
     *
     * @param fsmClazz
     * @param paramTypes
     * @return
     */
    public static StateMachineBuilderProxy create(Class<? extends StateMachineProxy> fsmClazz,
                                                  Class<?>... paramTypes) {
        final StateMachineBuilder<StateMachineProxy, Object, Object, Object> builder =
                create(fsmClazz, Object.class, Object.class, Object.class, paramTypes);
        return (StateMachineBuilderProxy) Proxy.newProxyInstance(
                StateMachineBuilderProxy.class.getClassLoader(),
                new Class[]{StateMachineBuilderProxy.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        try {
                            String methodName = method.getName();
                            if (methodName.equals("newDefaultStateMachine") || methodName.equals("newSoftStateMachine")) {
                                Object fsmInstance = null;
                                if (args.length == 3 && StateMachineConfig.class.isAssignableFrom(args[1].getClass()) &&
                                        (args[2] == null || args[2].getClass().isArray())) {
                                    fsmInstance = builder.newStateMachine(args[0], (StateMachineConfig) args[1], (Object[]) args[2]);
                                } else if (args.length == 2 && (args[1] == null || args[1].getClass().isArray())) {
                                    fsmInstance = builder.newStateMachine(args[0], (Object[]) args[1]);
                                } else if (args.length == 1) {
                                    fsmInstance = builder.newStateMachine(args[0]);
                                } else {
                                    throw new StateMachineException("Illegal argument number.");
                                }
                                return fsmInstance;
                            }
                            return method.invoke(builder, args);
                        } catch (InvocationTargetException e) {
                            throw e.getTargetException();
                        }
                    }
                });
    }


    /**
     * 根据AbstractStateMachine来创建
     *
     * @param fsmClazz
     * @param stateClazz
     * @param eventClazz
     * @param contextClazz
     * @param extraConstParamTypes
     * @param <T>
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <T extends StateMachine<T, S, E, C>, S, E, C> StateMachineBuilder<T, S, E, C> create(
            Class<? extends T> fsmClazz, Class<S> stateClazz, Class<E> eventClazz, Class<C> contextClazz,
            Class<?>... extraConstParamTypes) {
        return DependProvider.getInstance().newInstance(new TypeReference<StateMachineBuilder<T, S, E, C>>() {
                                                        },
                new Class[]{Class.class, Class.class, Class.class, Class.class, Class[].class},
                new Object[]{fsmClazz, stateClazz, eventClazz, contextClazz, extraConstParamTypes});
    }

    public static <T extends StateMachine<T, S, E, C>, S, E, C> StateMachineBuilder<T, S, E, C> create(
            Class<? extends T> stateMachineClazz, Class<S> stateClazz, Class<E> eventClazz, Class<C> contextClazz) {
        return create(stateMachineClazz, stateClazz, eventClazz, contextClazz, new Class<?>[0]);
    }
}
