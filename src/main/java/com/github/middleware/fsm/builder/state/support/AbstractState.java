package com.github.middleware.fsm.builder.state.support;

import com.github.middleware.fsm.builder.state.StateDefine;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.StateMachines;
import com.github.middleware.fsm.builder.exec.dto.ExecServiceContext;
import com.github.middleware.fsm.builder.chain.TransitionDefine;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.builder.state.dto.StateContext;
import com.github.middleware.fsm.core.utils.JsonUtil;
import com.github.middleware.fsm.exception.StateMachineException;
import com.github.middleware.fsm.filter.validator.support.PreCheckers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public abstract class AbstractState<T extends StateMachine<T, S, E, C>, S, E, C> implements StateDefine<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractState.class);
    private S name;
    private int level = 0;
    private boolean isEnd = false;
    private LinkedListMultimap<E, TransitionDefine<T, S, E, C>> transitions;
    private List<Action<T, S, E, C>> entryActions;
    private List<Action<T, S, E, C>> exitActions;
    private StateDefine<T, S, E, C> parentState;
    private StateDefine<T, S, E, C> initialState;
    private List<StateDefine<T, S, E, C>> childStates;
    private Set<E> acceptableEvents;
    //private ActionExecService actionExecService;

    public AbstractState(S name) {
        this.name = name;
        this.transitions = LinkedListMultimap.create();
        this.entryActions = Lists.newArrayList();
        this.exitActions = Lists.newArrayList();
        //this.actionExecService = StateMachines.newActionExecService();
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setParentState(StateDefine<T, S, E, C> parent) {
        if (this == parent) {
            throw new StateMachineException("非法操作：父节点不能设置为本身!");
        }
        if (this.parentState == null) {
            this.parentState = parent;
            setLevel(this.parentState != null ? this.parentState.getLevel() + 1 : 1);
        } else {
            throw new StateMachineException("非法操作：不能改变当前的父节点!");
        }
    }

    @Override
    public void setInitialState(StateDefine<T, S, E, C> childState) {
        if (this.initialState == null) {
            this.initialState = childState;
        } else {
            throw new StateMachineException("非法操作：不能改变当前初始状态!");
        }
    }

    @Override
    public void setEndState(boolean endState) {
        isEnd = endState;
    }

    @Override
    public void addChildState(StateDefine<T, S, E, C> childState) {
        if (childState != null) {
            if (childStates == null) {
                childStates = Lists.newArrayList();
            }
            if (!childStates.contains(childState))
                childStates.add(childState);
        }
    }

    @Override
    public TransitionDefine<T, S, E, C> addTransitionOn(E event) {
        TransitionDefine<T, S, E, C> newTransition = StateMachines.newTransition();
        newTransition.setFromState(this);
        newTransition.setEvent(event);
        addTransition(newTransition);
        return newTransition;
    }

    @Override
    public void check() {
        if (isEndState() && hasChildStates()) {
            throw new StateMachineException("校验异常：终结点不能用于子节点.");
        }
        if (transitions != null) {
            for (TransitionDefine<T, S, E, C> t : transitions.values()) {
                TransitionDefine<T, S, E, C> checkTransition = checkTransitions(t);
                if (checkTransition != null) {
                    throw new StateMachineException(String.format("校验异常：状态扭转冲突 '%s'->'%s'.", t, checkTransition));
                }
            }
        }
    }

    private TransitionDefine<T, S, E, C> checkTransitions(TransitionDefine<T, S, E, C> target) {
        for (TransitionDefine<T, S, E, C> t : transitions.values()) {
            if (t.getPreChecker() == null || target == t || t.getPreChecker().getClass() == PreCheckers.Never.class)
                continue;
            if (t.isEqual(target.getFromState().getName(), target.getToState().getName(), target.getEvent())) {
                if (t.getPreChecker().getClass() == PreCheckers.Always.class)
                    return target;
                if (target.getPreChecker().getClass() == PreCheckers.Always.class)
                    return target;
                if (t.getPreChecker().getName().equals(target.getPreChecker().getName()))
                    return target;
            }
        }
        return null;
    }

    @Override
    public S getName() {
        return name;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public StateDefine<T, S, E, C> getParentState() {
        return this.parentState;
    }

    @Override
    public StateDefine<T, S, E, C> getInitialState() {
        return this.initialState;
    }

    @Override
    public boolean isEndState() {
        return isEnd;
    }

    @Override
    public List<StateDefine<T, S, E, C>> getChildStates() {
        return this.childStates;
    }

    @Override
    public boolean hasChildStates() {
        return childStates != null && childStates.size() > 0;
    }

    @Override
    public boolean isRootState() {
        return parentState == null;
    }

    @Override
    public List<TransitionDefine<T, S, E, C>> getTransitions() {
        return Lists.newArrayList(transitions.values());
    }

    @Override
    public List<TransitionDefine<T, S, E, C>> getTransitions(E event) {
        return Lists.newArrayList(transitions.get(event));
    }

    @Override
    public StateDefine<T, S, E, C> self() {
        return this;
    }

    @Override
    public String getPath() {
        String currentId = name.toString();
        if (parentState == null) {
            return currentId;
        } else {
            return parentState.getPath() + "/" + currentId;
        }
    }

    @Override
    public Set<E> getEvents() {
        if (acceptableEvents == null) {
            Set<E> events = Sets.newHashSet();
            events.addAll(transitions.keySet());
            acceptableEvents = Collections.unmodifiableSet(events);
        }
        return acceptableEvents;
    }

    public void addTransition(TransitionDefine transition) {
        if (transition != null) {
            this.transitions.put((E) transition.getEvent(), transition);
        }
    }

    @Override
    public AbstractState<T, S, E, C> addEntryAction(Action entryAction) {
        this.entryActions.add(entryAction);
        return this;
    }

    @Override
    public AbstractState<T, S, E, C> addExitAction(Action exitAction) {
        this.exitActions.add(exitAction);
        return this;
    }

    @Override
    public List<Action<T, S, E, C>> getEntryActions() {
        return this.entryActions;
    }

    @Override
    public List<Action<T, S, E, C>> getExitActions() {
        return this.exitActions;
    }

    @Override
    public void fireInternal(StateContext<T, S, E, C> stateContext) {
        List<TransitionDefine<T, S, E, C>> transitions = getTransitions(stateContext.getEvent());
//        Precondition.checkState(transitions.size() == 1, "校验异常：触发事件与初始化状态有误！");
//        for (TransitionDefine<T, S, E, C> transition : transitions) {
//            if (transition.getEvent().equals(stateContext.getEvent())) {
//                LOGGER.info("fsm.fire stateFlow:" + this.getName() + ">" + transition.getToState() + "[" + stateContext.getEvent() + "]");
//                transition.fireInternal(stateContext);
//            }
//        }
        if (transitions.size() > 0) {
            TransitionDefine<T, S, E, C> transition = transitions.get(0);
            LOGGER.info("fsm.fire stateFlow:" + this.getName() + ">" + transition.getToState() + "[" + stateContext.getEvent() + "]");
            transition.fireInternal(stateContext);
            if (transitions.size() > 1) {
                LOGGER.warn("校验异常：检索到多条扭转记录！" + JsonUtil.json(transitions));
            }
        }
    }

    @Override
    public void enter(StateContext<T, S, E, C> stateContext) {
        LOGGER.info("fsm.fire 状态" + getName() + " 进入.such count:" + getEntryActions().size());
        ExecServiceContext execServiceContext = new ExecServiceContext().setExecServiceName("STATE_ENTRY_" + getName());
        for (Action<T, S, E, C> entryAction : getEntryActions()) {
            execServiceContext.setAction(entryAction).setFrom(null).setTo(getName()).setEvent(stateContext.getEvent()).setContext(stateContext.getContext()).setStateMachine(stateContext.getStateMachine().getThis());
            stateContext.getExecutor().execute(execServiceContext);
        }
    }

    @Override
    public void exit(final StateContext<T, S, E, C> stateContext) {
        LOGGER.info("fsm.fire 状态 " + getName() + " 退出.such count:" + getExitActions().size());
        ExecServiceContext execServiceContext = new ExecServiceContext().setExecServiceName("STATE_EXIT_" + getName());
        if (isEndState()) {
            LOGGER.info("fsm.fire 状态[" + getName() + "]未执行,因为它是终结点");
            return;
        }
        for (Action<T, S, E, C> exitAction : getExitActions()) {
            execServiceContext.setAction(exitAction).setFrom(getName()).setTo(null).setEvent(stateContext.getEvent()).setContext(stateContext.getContext()).setStateMachine(stateContext.getStateMachine().getThis());
            stateContext.getExecutor().execute(execServiceContext);
        }
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}
