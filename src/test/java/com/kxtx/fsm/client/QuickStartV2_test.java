package com.kxtx.fsm.client;

import com.kxtx.fsm.builder.dto.Order;
import com.kxtx.fsm.client.constants.OrderEvent;
import com.kxtx.fsm.client.constants.OrderState;
import com.kxtx.fsm.client.v2.OrderStateMachineV2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by netuser on 17-6-6.
 */
@ContextConfiguration("classpath*:spring-statemachine.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class QuickStartV2_test {
    @Autowired
    private OrderStateMachineV2 orderStateMachine;

    @Test
    public void go_handleEvent() {
        Order order = new Order() {{
            setId(1);
            setName("xiaomi5");
            setPrice(new BigDecimal(1));
            //setPrice(new BigDecimal(100000));
        }};
        orderStateMachine.handleEvent(OrderEvent.ATA1, order);
        Assert.assertTrue(orderStateMachine.getCurrentState() == OrderState.A1);
    }

}
