package com.kxtx.fsm.builder.event.dto;

import com.google.common.collect.Lists;
import com.kxtx.fsm.builder.event.BaseEvent;
import com.kxtx.fsm.builder.event.BaseListener;
import com.kxtx.fsm.builder.plugin.PluginProvider;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/16
 */
public class ListenerMappingContext implements Serializable {
    private PluginProvider pluginProvider;
    private List<ListenerMappingContextItem> mappingContextItems;

    public ListenerMappingContext() {
        this(null);
    }

    public ListenerMappingContext(PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
        mappingContextItems = Lists.newArrayList();
    }

    public PluginProvider getPluginProvider() {
        return pluginProvider;
    }

    public void setPluginProvider(PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    public List<ListenerMappingContextItem> getMappingContextItems() {
        return mappingContextItems;
    }

    public void addMappingContextItem(ListenerMappingContextItem mappingContextItem) {
        if (mappingContextItem != null) this.mappingContextItems.add(mappingContextItem);
    }

    @Override
    public String toString() {
        return "ListenerMappingContext{" +
                "pluginProvider=" + pluginProvider +
                ", mappingContextItems=" + mappingContextItems +
                '}';
    }
}
