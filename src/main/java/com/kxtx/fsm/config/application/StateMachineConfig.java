package com.kxtx.fsm.config.application;

import com.kxtx.fsm.core.Singletonable;
import com.kxtx.fsm.core.utils.IDProvider;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public final class StateMachineConfig extends AbstractConfig implements Singletonable {
    /**
     * 是否开启事件监听
     */
    private boolean enableEventListener = true;
    private IDProvider idProvider = IDProvider.Default.getInstance();

    public IDProvider getIdentifier() {
        return idProvider;
    }

    public StateMachineConfig setIdentifier(IDProvider idProvider) {
        this.idProvider = idProvider;
        return this;
    }

    public boolean enableEventListener() {
        return enableEventListener;
    }

    public StateMachineConfig setEnableEventListener(boolean enableEventListener) {
        this.enableEventListener = enableEventListener;
        return this;
    }
}
