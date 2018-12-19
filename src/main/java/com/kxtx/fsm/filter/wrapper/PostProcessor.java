package com.kxtx.fsm.filter.wrapper;

/**
 * 后置处理器
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
@FunctionalInterface
public interface PostProcessor<T> {
    void postProcess(T Obj);
}
