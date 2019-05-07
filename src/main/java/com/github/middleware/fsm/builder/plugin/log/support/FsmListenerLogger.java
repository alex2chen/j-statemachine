package com.github.middleware.fsm.builder.plugin.log.support;

import com.github.middleware.fsm.builder.event.annotation.*;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.builder.plugin.PluginProvider;
import com.github.middleware.fsm.exception.StateMachineException;
import com.google.common.base.Stopwatch;
import com.github.middleware.fsm.builder.plugin.support.AbstractPluginProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/15
 */

public class FsmListenerLogger extends AbstractPluginProvider implements PluginProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(FsmListenerLogger.class);
    private Stopwatch transitionWatch;
    private Stopwatch actionWatch;

    public FsmListenerLogger() {
        name = "log";
    }
    @OnTransitionBegin
    public void onTransitionBegin(Object from, Object event, Object context) {
        transitionWatch = Stopwatch.createStarted();
        LOGGER.info(fsmId + ": 扭转 from[" + from + "] on [" + event + "] with context [" + context + "] begin.");
    }

    @OnTransitionComplete
    public void onTransitionComplete(Object sourceState, Object targetState, Object event, Object context) {
        LOGGER.info(fsmId + ": 扭转 from [" + sourceState + "] to [" + targetState + "] on [" + event + "] complete which took " + transitionWatch + ".");
    }

    @OnTransitionDecline
    public void onTransitionDeclined(Object sourceState, Object event) {
        LOGGER.info(fsmId + ": 扭转 from [" + sourceState + "] on [" + event + "] 拒绝.");
    }

    @OnTransitionException
    public void onTransitionException(Object sourceState, Object targetState, Object event, Object context, StateMachineException e) {
        Throwable expcetion = e.getTargetException();
        LOGGER.error(fsmId + ": 扭转 from [" + sourceState + "] to [" + targetState + "] on [" + event + "] 发送异常.", expcetion);
    }

    @OnBeforeAction(when = "1==2")
    public void onBeforeActionExecuted(Object sourceState, Object targetState,
                                       Object event, Object context, int[] mOfN, Action<?, ?, ?, ?> action) {
        actionWatch = Stopwatch.createStarted();
        LOGGER.info("方法调用前 [" + action.getName() + "]:" + action.weight() + " (" + mOfN[0] + " of " + mOfN[1] + ").");
    }

    @OnAfterAction(when = "1==1")
    public void onAfterActionExecuted(Object sourceState, Object targetState,
                                      Object event, Object context, int[] mOfN, Action<?, ?, ?, ?> action) {
        LOGGER.info("方法调用后 [" + action.getName() + "]:" + action.weight() + " which took " + actionWatch + ".");
    }

    @OnActionException
    public void onActionExecException(Action<?, ?, ?, ?> action, StateMachineException e) {
        LOGGER.error("方法调用异常 [" + action.getName() + "] caused by " + e.getMessage());
    }

}
