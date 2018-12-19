package com.kxtx.fsm.filter.wrapper.support;

import com.google.common.collect.Lists;
import com.kxtx.fsm.filter.wrapper.ComposPostProcessor;
import com.kxtx.fsm.filter.wrapper.PostProcessor;

import java.util.List;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class ComposPostProcessorImpl<T> implements ComposPostProcessor<T> {
    private List<PostProcessor<? super T>> processors;

    public ComposPostProcessorImpl() {
        this(null);
    }

    public ComposPostProcessorImpl(PostProcessor<? super T> processors) {
        this.processors = Lists.newArrayList(processors);
    }

    @Override
    public void compose(PostProcessor<? super T> processor) {
        if (!processors.contains(processor)) {
            processors.add(processor);
        }
    }

    @Override
    public void postProcess(T Obj) {
        for (PostProcessor<? super T> processor : processors) {
            processor.postProcess(Obj);
        }
    }
}
