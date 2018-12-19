package com.kxtx.fsm.core;

import com.google.common.collect.Maps;
import com.kxtx.fsm.builder.dto.Order;
import com.kxtx.fsm.core.el.MvelScriptManager;
import com.kxtx.fsm.core.factory.DependProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/12
 */
public class MvelScriptManager_test {
    private MvelScriptManager mvelScriptManager;
    private Order order = new Order(1, "ipad", new BigDecimal(2399));
    private Map vars = new HashMap();

    @Before
    public void initInstance() {
        mvelScriptManager = DependProvider.getInstance().newInstance(MvelScriptManager.class);
        vars.put("x", new Integer(5));
        vars.put("y", new Integer(10));
    }

    @Test
    public void go_evalBoolean() {
        boolean isEq = mvelScriptManager.evalBoolean("name=='ipad'", order);
        Assert.assertTrue(isEq);
    }

    @Test
    public void go_eval() {
        //get
        String name = mvelScriptManager.eval("name", order, String.class);
        Assert.assertTrue(name.equals("ipad"));
        //calc
        Integer calc = mvelScriptManager.eval("x*y", vars, Integer.class);
        Assert.assertTrue(calc == 50);
    }

    @Test
    public void go_compile() {
        mvelScriptManager.compile("x*y");
        Integer calc = mvelScriptManager.eval("x*y", vars, Integer.class);
        Assert.assertTrue(calc == 50);
    }

}
