package com.kxtx.fsm.client.v2;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.dto.Order;
import com.kxtx.fsm.builder.machine.support.visit.StateMachineProxy;
import com.kxtx.fsm.client.constants.OrderEvent;
import com.kxtx.fsm.client.constants.OrderState;
import com.kxtx.fsm.config.annotation.*;

/**
 * Created by netuser on 17-6-6.
 */
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
@StateMachineParameters(stateType = OrderState.class, eventType = OrderEvent.class, contextType = Order.class)
public interface OrderStateMachineV2 extends StateMachineProxy {
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
