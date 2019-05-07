package com.github.middleware.fsm.filter.wrapper;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public interface ComposPostProcessor<T> extends PostProcessor<T> {
    void compose(PostProcessor<? super T> processor);
}
