package com.github.middleware.fsm.core.convert;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/3
 */
public interface Converter<T> {
    String objToString(T obj);

    T stringToObj(String name);
}
