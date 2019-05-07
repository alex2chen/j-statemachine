package com.github.middleware.fsm.core.convert;

import com.github.middleware.fsm.core.factory.DependProvider;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public interface ConverterProvider {
    static ConverterProvider INSTANCE = DependProvider.getInstance().newInstance(ConverterProvider.class);
    void register(Class<?> clazz, Converter<?> converter);
    void register(Class<?> clazz, Class<? extends Converter<?>> converterClass);
    void clear();
    <T> Converter<T> getConverter(Class<T> clazz);
}
