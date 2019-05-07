package com.github.middleware.fsm.filter;

import com.github.middleware.fsm.core.el.MvelScriptManager;
import com.github.middleware.fsm.core.factory.DependProvider;
import com.github.middleware.fsm.filter.validator.PreChecker;
import com.github.middleware.fsm.filter.validator.support.MvelCheckerImpl;
import com.github.middleware.fsm.builder.dto.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/10
 */
public class MvelChecker_test {
    private PreChecker preChecker;

    @Before
    public void initInstance() {
        MvelScriptManager mvelScriptManager = DependProvider.getInstance().newInstance(MvelScriptManager.class);
        preChecker = new MvelCheckerImpl<Order>("EqChecker::context.name=='ipad'", mvelScriptManager);
    }

    @Test
    public void go_eval_when() {
        Order order = new Order(1, "ipad", new BigDecimal(100000));
        boolean isEq = preChecker.validate(order);
        Assert.assertTrue(isEq);
    }
}
