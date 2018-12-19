package com.kxtx.fsm.builder.plugin;

import com.google.common.collect.Multiset;
import com.kxtx.fsm.core.Singletonable;

/**
 * Created by YT on 2017/3/15.
 */
public interface PluginProvider extends Singletonable{
    String getPluginName();
    String  setFsmId(String fsmId);
}
