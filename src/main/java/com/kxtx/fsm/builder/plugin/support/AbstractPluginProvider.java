package com.kxtx.fsm.builder.plugin.support;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.plugin.PluginProvider;
import com.kxtx.fsm.filter.annotation.PostConstruct;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/17
 */
public abstract class AbstractPluginProvider implements PluginProvider {
    protected String name;
    protected String fsmId;

    @Override
    public String getPluginName() {
        return "plugin_" + name;
    }

    /**
     * 暂时不需要整个状态机注入
     * @param fsmId
     * @return
     */
    @PostConstruct
    public String setFsmId(String fsmId) {
        return this.fsmId = fsmId;
    }
}
