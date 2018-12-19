package com.kxtx.fsm.builder.exec.support;

import com.google.common.collect.Maps;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.event.BeforeExecActionEvent;
import com.kxtx.fsm.builder.event.BeforeExecActionListener;
import com.kxtx.fsm.builder.event.support.AfterExecActionEventImpl;
import com.kxtx.fsm.builder.event.support.BeforeExecActionEventImpl;
import com.kxtx.fsm.builder.event.support.ExecActionExceptionEventImpl;
import com.kxtx.fsm.builder.exec.Action;
import com.kxtx.fsm.builder.exec.ActionExecService;
import com.kxtx.fsm.builder.exec.dto.ExecServiceContext;
import com.kxtx.fsm.builder.machine.dto.StateMachineContext;
import com.kxtx.fsm.builder.machine.support.AbstractStateMachine;
import com.kxtx.fsm.builder.machine.support.AbstractSubject;
import com.kxtx.fsm.config.application.StateMachineConfig;
import com.kxtx.fsm.core.factory.ThreadExecutorService;
import com.kxtx.fsm.core.utils.Precondition;
import com.kxtx.fsm.exception.StateMachineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public abstract class AbstractExecService<T extends StateMachine<T, S, E, C>, S, E, C> extends AbstractSubject implements ActionExecService<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecService.class);
    private StateMachineConfig config;
    private String identifier;

    public AbstractExecService(StateMachineConfig config) {
        this.config = config;
    }

    @Override
    public void execute(ExecServiceContext execServiceContext) {
        if (execServiceContext == null || execServiceContext.getStateMachine() == null || execServiceContext.getAction() == null) {
            LOGGER.warn("AbstractExecService.execute发送意外提前终止执行.");
            return;
        }
        String methodName = execServiceContext.getAction().getName();
        if (execServiceContext.getAction().weight() == Action.WEIGHT_IGNORE) {
            LOGGER.warn(identifier + "方法" + methodName + "被忽略执行.");
            return;
        }
        doExecuteFlow(execServiceContext, execServiceContext.getAction().isAsync());
    }

    private void doExecuteFlow(ExecServiceContext execServiceContext, boolean isAsync) {
        try {
            fireEvent(new BeforeExecActionEventImpl(execServiceContext));
            if (!isAsync) {
                execServiceContext.doAction();
            } else {
                Map<ExecServiceContext<T, S, E, C>, Future<?>> futures = Maps.newHashMap();
                boolean isTestEvent = StateMachineContext.isTestEvent();
                T instance = StateMachineContext.getTnstance();
                Future<?> futureExec = ThreadExecutorService.getExecutor().submit(() -> {
                    StateMachineContext.setTnstance(instance, isTestEvent);
                    try {
                        execServiceContext.doAction();
                    } finally {
                        StateMachineContext.setTnstance(null);
                    }
                });
                futures.put(execServiceContext, futureExec);
                ExecServiceContext execSContext;
                for (Map.Entry<ExecServiceContext<T, S, E, C>, Future<?>> entry : futures.entrySet()) {
                    Future<?> future = entry.getValue();
                    execSContext = entry.getKey();
                    try {
                        LOGGER.debug(identifier + "调用方法" + execSContext.getAction().toString() + "等待完成.");
                        if (execSContext.getAction().timeout() >= 0) {
                            future.get(execSContext.getAction().timeout(), TimeUnit.MILLISECONDS);
                        } else {
                            future.get();
                        }
                        LOGGER.debug(identifier + "调用方法" + execSContext.getAction().toString() + "完成.");
                    } catch (Exception e) {
                        future.cancel(true);
                        LOGGER.error(identifier + "调用AbstractExecService.execute future get异常:", e);
                    }
                }
            }
            fireEvent(new AfterExecActionEventImpl(execServiceContext));
        } catch (Exception ex) {
            StateMachineException exception = (ex instanceof StateMachineException) ? (StateMachineException) ex : new StateMachineException(ex.getMessage());
            fireEvent(new ExecActionExceptionEventImpl(execServiceContext, exception));
            throw exception;
        }
    }

    @Override
    public void addExecActionListener(BeforeExecActionListener<T, S, E, C> listener) {
        addListener(BeforeExecActionEvent.class, listener, BeforeExecActionListener.METHOD);
    }

    @Override
    public void removeExecActionListener(BeforeExecActionListener<T, S, E, C> listener) {
        removeListener(BeforeExecActionEvent.class, listener);
    }

    @Override
    public void addProxyListener(Class<?> eventType, Object listener, Method method) {
        super.addListener(eventType, listener, method);
    }

    @Override
    protected boolean enableEventListener() {
        return config == null ? false : config.enableEventListener();
    }
}
