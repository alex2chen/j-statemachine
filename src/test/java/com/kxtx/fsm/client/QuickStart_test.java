package com.kxtx.fsm.client;

import com.google.common.base.Stopwatch;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.dto.Order;
import com.kxtx.fsm.builder.plugin.log.LogExec;
import com.kxtx.fsm.builder.machine.StateMachineBuilder;
import com.kxtx.fsm.builder.machine.factroy.StateMachineBuilderFactory;
import com.kxtx.fsm.builder.machine.support.AbstractStateMachine;
import com.kxtx.fsm.builder.plugin.log.support.FsmListenerLogger;
import com.kxtx.fsm.client.constants.OrderEvent;
import com.kxtx.fsm.client.constants.OrderState;
import com.kxtx.fsm.config.annotation.State;
import com.kxtx.fsm.config.annotation.States;
import com.kxtx.fsm.config.annotation.Transition;
import com.kxtx.fsm.config.annotation.Transitions;
import com.kxtx.fsm.config.application.StateMachineConfig;
import com.kxtx.fsm.core.utils.IDProvider;
import com.kxtx.fsm.filter.wrapper.PostProcessor;
import com.kxtx.fsm.filter.wrapper.PostProcessorProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/9
 */
public class QuickStart_test {
    @States({
            @State(name = "A", isInitial = true),
            @State(name = "A1", entryMethod = "enterA1", exitMethod = "exitA1"),
            @State(name = "A2", entryMethod = "enterA2", exitMethod = "exitA2"),
            @State(name = "A3", entryMethod = "enterA3", exitMethod = "exitA3"),
            @State(name = "A4", entryMethod = "enterA4", exitMethod = "exitA4"),
            @State(name = "A4a", parent = "A4", entryMethod = "enterA4a", exitMethod = "exitA5a", isEnd = true)
    })
    @Transitions({
            @Transition(from = "A", to = "A1", on = "ATA1", complete = "fromAToA1", whenMVEL = "PriceChecker::context.price>0&&context.price<100000"),
            @Transition(from = "A1", to = "A2", on = "A1TA2", complete = "fromA1ToA2"),
            @Transition(from = "A2", to = "A3", on = "A2TA3", complete = "fromA2ToA3"),
            @Transition(from = "A3", to = "A4", on = "A3TA4", complete = "fromA3ToA4")
    })
    interface OrderStateMachine extends StateMachine<OrderStateMachine, OrderState, OrderEvent, Order> {
        // entry states
        void enterA1(OrderState from, OrderState to, OrderEvent event, Order context);

        void enterA2(OrderState from, OrderState to, OrderEvent event, Order context);

        void enterA3(OrderState from, OrderState to, OrderEvent event, Order context);

        void enterA4(OrderState from, OrderState to, OrderEvent event, Order context);

        void enterA4a(OrderState from, OrderState to, OrderEvent event, Order context);

        // transitions
        void fromAToA1(OrderState from, OrderState to, OrderEvent event, Order context);

        void fromA1ToA2(OrderState from, OrderState to, OrderEvent event, Order context);

        void fromA2ToA3(OrderState from, OrderState to, OrderEvent event, Order context);

        void fromA3ToA4(OrderState from, OrderState to, OrderEvent event, Order context);

        // exit states
        void exitA1(OrderState from, OrderState to, OrderEvent event, Order context);

        void exitA2(OrderState from, OrderState to, OrderEvent event, Order context);

        void exitA3(OrderState from, OrderState to, OrderEvent event, Order context);

        void exitA4(OrderState from, OrderState to, OrderEvent event, Order context);

        void beforeTransitionBegin(OrderState from, OrderEvent event, Order context);

        void afterTransitionCompleted(OrderState from, OrderState to, OrderEvent event, Order context);

        void afterTransitionDeclined(OrderState from, OrderEvent event, Order context);

        void afterTransitionCausedException(OrderState fromState, OrderState toState, OrderEvent event, Order context);

    }

    public static class OrderStateMachineImpl extends AbstractStateMachine<OrderStateMachine, OrderState, OrderEvent, Order> implements OrderStateMachine {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @LogExec
        @Override
        public void enterA1(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void enterA2(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void enterA3(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void enterA4(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void enterA4a(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void fromAToA1(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void fromA1ToA2(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void fromA2ToA3(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println(this.name);
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void fromA3ToA4(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void exitA1(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void exitA2(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void exitA3(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void exitA4(OrderState from, OrderState to, OrderEvent event, Order context) {
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void beforeTransitionBegin(OrderState from, OrderEvent event, Order context) {
            //order do
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
            super.beforeTransitionBegin(from, event, context);
        }

        @Override
        public void afterTransitionCompleted(OrderState from, OrderState to, OrderEvent event, Order context) {
            //order do
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
            super.afterTransitionCompleted(from, to, event, context);
        }

        @Override
        public void afterTransitionDeclined(OrderState from, OrderEvent event, Order context) {
            //order do
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
            super.afterTransitionDeclined(from, event, context);
        }

        @Override
        public void afterTransitionCausedException(OrderState fromState, OrderState toState, OrderEvent event, Order context) {
            //order do
            System.out.println("当前执行方法：" + Thread.currentThread().getStackTrace()[1].getMethodName());
            super.afterTransitionCausedException(fromState, toState, event, context);
        }
    }

    private StateMachineBuilder<OrderStateMachine, OrderState, OrderEvent, Order> builder;
    private OrderStateMachine stateMachine;
    private Stopwatch stopwatch;

    @Before
    public void beforeMethod() {
        builder = StateMachineBuilderFactory.create(OrderStateMachineImpl.class, OrderState.class, OrderEvent.class, Order.class);
        builder.newPluginBuilder(new FsmListenerLogger()).install();
        stopwatch = Stopwatch.createStarted();
    }

    @Test
    public void go_handleEvent() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Order order = new Order() {{
            setId(1);
            setName("xiaomi5");
            setPrice(new BigDecimal(1));
            //setPrice(new BigDecimal(100000));
        }};
        stateMachine = builder.newStateMachine(OrderState.A, new StateMachineConfig().setEnableEventListener(true));
        stateMachine.handleEvent(OrderEvent.ATA1, order);
        System.out.println("times:" + stopwatch);
        Assert.assertTrue(stateMachine.getCurrentState() == OrderState.A1);
    }

    @Test
    public void go_handleEvent_useBean() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Order order = new Order() {{
            setId(1);
            setName("xiaomi5");
            setPrice(new BigDecimal(1));
            //setPrice(new BigDecimal(100000));
        }};
        OrderStateMachineImpl beanInstance = new OrderStateMachineImpl();
        beanInstance.setName("a");
        stateMachine = builder.newStateMachine(beanInstance, OrderState.A, new StateMachineConfig().setEnableEventListener(true));
        stateMachine.handleEvent(OrderState.A2, OrderEvent.A2TA3, order);
        System.out.println(beanInstance == stateMachine);
        Assert.assertTrue(stateMachine.getCurrentState() == OrderState.A3);
    }

    @Test
    public void go_handleEvent_plugin() {
        Order order = new Order() {{
            setId(1);
            setName("xiaomi5");
            setPrice(new BigDecimal(1));
            //setPrice(new BigDecimal(100000));
        }};
        PostProcessor<OrderStateMachine> pluginPostProceesor = (m) -> {
            m.pluginsStart();
        };
        PostProcessorProvider.getInstance().register(OrderStateMachine.class, pluginPostProceesor);

        stateMachine = builder.newStateMachine(OrderState.A, new StateMachineConfig().setEnableEventListener(true));
        stateMachine.handleEvent(OrderEvent.ATA1, order);
        Assert.assertTrue(stateMachine.getCurrentState() == OrderState.A1);
        System.out.println("times:" + stopwatch);
    }

}
