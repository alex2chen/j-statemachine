package com.github.middleware.fsm.client.v2;

import com.github.middleware.fsm.builder.dto.Order;
import com.github.middleware.fsm.client.constants.OrderEvent;
import com.github.middleware.fsm.client.constants.OrderState;
import com.github.middleware.fsm.spring.annotation.StatemachineService;
import com.github.middleware.fsm.builder.machine.support.visit.AbstractStateMachineProxy;
import com.github.middleware.fsm.builder.plugin.log.LogExec;

/**
 * Created by netuser on 17-6-6.
 */
@StatemachineService
public class OrderStateMachineImplV2 extends AbstractStateMachineProxy implements OrderStateMachineV2 {
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
