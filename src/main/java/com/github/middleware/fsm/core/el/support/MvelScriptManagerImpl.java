package com.github.middleware.fsm.core.el.support;

import com.github.middleware.fsm.core.el.MvelScriptManager;
import org.mvel2.MVEL;
import org.mvel2.optimizers.OptimizerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/9
 */
public class MvelScriptManagerImpl implements MvelScriptManager {

    private Map<String, Object> expressions;
    public MvelScriptManagerImpl(){
        OptimizerFactory.setDefaultOptimizer("reflective");
    }
    @Override
    public <T> T eval(String script, Object context, Class<T> returnType) {
        Object evaluateResult = null;
        if (getCompiledExpression().containsKey(script)) {
            Object exp = getCompiledExpression().get(script);
            evaluateResult = MVEL.executeExpression(exp, context);
        } else {
            evaluateResult = MVEL.eval(script, context);
        }
        return returnType.cast(evaluateResult);
    }

    @Override
    public void compile(String script) {
        if (!getCompiledExpression().containsKey(script)) {
            Object compiled = MVEL.compileExpression(script);
            if (compiled != null) {
                getCompiledExpression().put(script, compiled);
            }
        }
    }

    private Map<String, Object> getCompiledExpression() {
        if (expressions == null)
            expressions = new ConcurrentHashMap<String, Object>();
        return expressions;
    }

    @Override
    public boolean evalBoolean(String script, Object context) {
        return eval(script, context, Boolean.class);
    }

}

