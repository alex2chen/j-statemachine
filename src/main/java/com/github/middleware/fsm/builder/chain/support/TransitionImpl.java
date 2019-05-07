package com.github.middleware.fsm.builder.chain.support;

import com.github.middleware.fsm.builder.chain.TransitionDefine;
import com.github.middleware.fsm.builder.state.StateDefine;
import com.github.middleware.fsm.builder.state.dto.StateContext;
import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.exec.dto.ExecServiceContext;
import com.github.middleware.fsm.exception.ApiObsoleteException;
import com.github.middleware.fsm.exception.StateMachineException;
import com.github.middleware.fsm.filter.validator.PreChecker;
import com.github.middleware.fsm.builder.exec.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
public class TransitionImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements TransitionDefine<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransitionImpl.class);
    private StateDefine<T, S, E, C> fromState;
    private StateDefine<T, S, E, C> toState;
    private E event;
    private PreChecker preChecker;
    private List<Action> actionList;
    //private ActionExecService actionExecService;

    public TransitionImpl() {
        this(null, null);
    }

    public TransitionImpl(StateDefine<T, S, E, C> fromState, StateDefine<T, S, E, C> toState) {
        this.fromState = fromState;
        this.toState = toState;
        //actionExecService = StateMachines.newActionExecService();
    }

    @Override
    public E getEvent() {
        return event;
    }

    @Override
    public StateDefine<T, S, E, C> getFromState() {
        return fromState;
    }

    @Override
    public StateDefine<T, S, E, C> getToState() {
        return toState;
    }

    @Override
    public PreChecker getPreChecker() {
        return preChecker;
    }

    @Override
    public List<Action> getActionList() {
        return actionList;
    }

    @Override
    public boolean isEqual(S fromState, S toState, E event) {
        if (toState == null && !getToState().isEndState())
            return false;
        if (toState != null && !getToState().isEndState() &&
                !getToState().getName().equals(toState)) {
            return false;
        }
        if (!getEvent().equals(event)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isEqual(S fromState, S toState, E event, Class<?> checker) {
        if (!isEqual(fromState, toState, event)) {
            return false;
        }
        if (getPreChecker() == null) {
            return false;
        }
        if (getPreChecker().getClass() != checker) {
            return false;
        }
        return true;
    }

    @Override
    public void setFromState(StateDefine<T, S, E, C> state) {
        if (this.fromState == null) {
            this.fromState = state;
        } else {
            throw new StateMachineException("非法操作：不能修改源状态!");
        }

    }

    @Override
    public void setToState(StateDefine<T, S, E, C> state) {
        if (this.toState == null) {
            this.toState = state;
        } else {
            throw new StateMachineException("非法操作：不能修改目标状态!");
        }
    }

    @Override
    public void setPreChecker(PreChecker<C> preChecker) {
        this.preChecker = preChecker;
    }

    @Override
    public void addAction(Action action) {
        if (actionList == null) actionList = new ArrayList<Action>();
        if (action != null) actionList.add(action);
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void fireInternal(StateContext<T, S, E, C> stateContext) {
        // LOGGER.info("fsm.fire transitionFlow:" + JSON.toJSONString(this));
        //TODO:preChecker是不是必须有空再斟酌下
        if (preChecker != null) {
            if (preChecker.validate(stateContext.getContext())) {
                fireFlow(stateContext);
            } else {
                throw new StateMachineException("操作失败：未通过业务校验.");
            }
        } else {
            fireFlow(stateContext);
        }
    }

    private void fireFlow(StateContext<T, S, E, C> stateContext) {
        //exitPreState(stateContext.getFromState(), stateContext);
        stateContext.getFromState().exit(stateContext);
        pass(stateContext);
        getToState().enter(stateContext);
        stateContext.getResult().setAccepted(true).setToState(getToState());
    }

    @Deprecated
    private void exitPreState(StateDefine<T, S, E, C> orgState, StateContext<T, S, E, C> stateContext) {
        for (StateDefine<T, S, E, C> state = orgState; state != getFromState(); state = state.getParentState()) {
            if (state != null) {
                state.exit(stateContext);
            }
        }
    }

    @Override
    public StateDefine<T, S, E, C> pass(StateContext<T, S, E, C> stateContext) {
        ExecServiceContext execServiceContext = new ExecServiceContext().setExecServiceName("TRANSITION_" + this.toString());
//        LOGGER.info("fsm.fire transitionFlow:" + fromState.getName() + ">" + toState.getName() + ",such count:" + getActionList().size());
        for (Action<T, S, E, C> action : getActionList()) {
            execServiceContext.setAction(action).setFrom(fromState.getName()).setTo(toState.getName()).setEvent(stateContext.getEvent()).setContext(stateContext.getContext()).setStateMachine(stateContext.getStateMachine().getThis());
            stateContext.getExecutor().execute(execServiceContext);
        }
        return toState;
    }


    @Override
    public TransitionImpl<T, S, E, C> to(S stateName) {
        throw new ApiObsoleteException("此接口已废弃！");
    }

    @Override
    public TransitionImpl<T, S, E, C> toChoice(S choiceName) {
        throw new ApiObsoleteException("此接口已废弃！");
    }

    @Override
    public TransitionImpl<T, S, E, C> onEvent(E event) {
        throw new ApiObsoleteException("此接口已废弃！");
    }

    @Override
    public TransitionImpl<T, S, E, C> preChecker(PreChecker preChecker) {
        throw new ApiObsoleteException("此接口已废弃！");
    }

    @Override
    public TransitionImpl<T, S, E, C> preCheckerElse() {
        //        if (this.preChecker != null)
        //            throw new IllegalPreCheckerException("非法操作： 校验已存在！");
        //        if (!(fromState instanceof ChoiceStateDefine))
        //            throw new IllegalPreCheckerException("非法操作：Else-PreChecker 不允许选择！");
        //        boolean hasRreCheckerTransition = false;
        //        for (TransitionDefine<T, S, E, C> transition : fromState.getTransitions()) {
        //            if (transition.getPreChecker() != null) {
        //                hasRreCheckerTransition = true;
        //                break;
        //            }
        //        }
        //        if (!hasRreCheckerTransition)
        //            throw new IllegalPreCheckerException("非法操作：Prev. PreChecker 未定义!");
        //        this.preChecker = null;
        //        return this;
        throw new ApiObsoleteException("此接口已废弃！");
    }

    @Override
    public TransitionImpl<T, S, E, C> withAction(Action action) {
        throw new ApiObsoleteException("此接口已废弃！");
    }

    @Override
    public final String toString() {
        return fromState + "->" + toState + "[" + event.toString() + "]";
    }

}
