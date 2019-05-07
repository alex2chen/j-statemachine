package com.github.middleware.fsm.builder.plugin;

import com.github.middleware.fsm.core.Singletonable;

/**
 * Created by YT on 2017/3/15.
 */
public interface PluginProvider extends Singletonable{
    String getPluginName();
    String  setFsmId(String fsmId);
}
