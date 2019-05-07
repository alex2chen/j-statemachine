package com.github.middleware.fsm.core;

import com.github.middleware.fsm.config.annotation.State;
import com.github.middleware.fsm.config.annotation.States;
import com.github.middleware.fsm.core.utils.ReflectUtil;
import com.github.middleware.fsm.filter.annotation.PostConstruct;
import com.google.common.base.Function;
import com.github.middleware.fsm.builder.machine.support.AbstractStateMachine;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/10
 */
public class ReflectUtil_test {
    @Test
    public void go_isStateMachineInterface() {
        Assert.assertFalse(ReflectUtil.isStateMachineInterface(OrderStateMachine.class));
    }

    @Test
    public void go_isStateMachineType() {
        Assert.assertTrue(ReflectUtil.isStateMachineType(OrderStateMachine.class));
    }

    @Test
    public void go_processStateMachineClass() {
        ReflectUtil.processStateMachineClass(OrderStateMachine.class, new DeclareStateFunction());

    }

    @Test
    public void go_getAnnotatedMethods() {
        List<Method> methods = ReflectUtil.getAnnotatedMethods(ReflectUtil_test.class, PostConstruct.class, new Class[]{String.class});
        Assert.assertTrue(methods.size() == 1);
    }

    @PostConstruct
    public void postConstruct(String str) {
    }

    @PostConstruct
    public void postConstruct(int num) {
    }


    private class DeclareStateFunction implements Function<Class<?>, Boolean> {
        @Override
        public Boolean apply(Class<?> k) {
            //buildDeclareState(k.getAnnotation(State.class));
            System.out.println(".");
            System.out.println(k.getAnnotation(State.class));
            States states = k.getAnnotation(States.class);
            if (states != null && states.value() != null) {
                for (State s : states.value()) {
                    //StateMachineBuilderImpl.this.buildDeclareState(s);
                    System.out.println("...");
                    System.out.println(s);
                }
            }
            return true;
        }
    }

    public enum OrderState {
        INIT,
        CREATE, PAY, SEND, SIGN, COMMENT, FINISH, END,
        //Choices
        AMOUNT_CHECK;
    }

    public enum OrderEvent {
        POSTORDER,
        ALIPAY,
        PAYMENT,
        SEND,
        SIGN,
        COMMENT,
        DOCHECK
    }

    @State(parent = "", name = "INIT", entryMethod = "enterInit", exitMethod = "leftInit")
    @States(@State(parent = "", name = "CREATE", entryMethod = "enterCREATE", exitMethod = "leftCREATE"))
    class OrderStateMachine extends AbstractStateMachine<OrderStateMachine, OrderState, OrderEvent, Integer> {

    }
}
