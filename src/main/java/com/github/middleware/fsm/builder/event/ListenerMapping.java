package com.github.middleware.fsm.builder.event;

import com.github.middleware.fsm.builder.event.annotation.*;

/**
 * Created by YT on 2017/3/16.
 */
public interface ListenerMapping {
    String LISTENER_ANNOTATION_WHEN_NAME = "when";
    String LISTENER_FIELD_NAME="METHOD";
    Class<?>[][] SATE_LISTENER_MAPPING = {
            {OnTransitionBegin.class, TransitionBeginListener.class, TransitionBeginEvent.class},
            {OnTransitionComplete.class, TransitionCompleteListener.class, TransitionCompleteEvent.class},
            {OnTransitionDecline.class, TransitionDeclinedListener.class, TransitionDeclinedEvent.class},
            {OnTransitionEnd.class, TransitionEndListener.class, TransitionEndEvent.class},
            {OnTransitionException.class, TransitionExceptionListener.class, TransitionExceptionEvent.class}
    };
    Class<?>[][] ACTION_LISTENR_MAPPING = {
            {OnBeforeAction.class, BeforeExecActionListener.class, BeforeExecActionEvent.class},
            {OnAfterAction.class, AfterExecActionListener.class, AfterExecActionEvent.class},
            {OnActionException.class, ExecActionExceptionListener.class, ExecActionExceptionEvent.class},
    };
}
