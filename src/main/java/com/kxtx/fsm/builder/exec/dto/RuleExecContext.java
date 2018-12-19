package com.kxtx.fsm.builder.exec.dto;

import com.kxtx.fsm.core.el.MvelScriptManager;

import java.io.Serializable;

/**
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class RuleExecContext implements Serializable {
    private MvelScriptManager scriptManager;
    private Class<?> executionTargetType;
    private Class<?>[] methodCallParamTypes;

    public RuleExecContext(MvelScriptManager scriptManager, Class<?> executionTargetType, Class<?>[] methodCallParamTypes) {
        this.scriptManager = scriptManager;
        this.executionTargetType = executionTargetType;
        this.methodCallParamTypes = methodCallParamTypes;
    }

    public MvelScriptManager getScriptManager() {
        return scriptManager;
    }

    public Class<?> getExecutionTargetType() {
        return executionTargetType;
    }

    public Class<?>[] getMethodCallParamTypes() {
        return methodCallParamTypes;
    }

}
