package com.kxtx.fsm.core.convert.support;

import com.google.common.collect.Maps;
import com.kxtx.fsm.core.convert.Converter;
import com.kxtx.fsm.core.convert.ConverterProvider;
import com.kxtx.fsm.core.factory.DependProvider;

import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class ConverterProviderImpl implements ConverterProvider {
    private Map<Class<?>, Converter<?>> converters = Maps.newHashMap();
    @Override
    public void register(Class<?> clazz, Converter<?> converter) {
        converters.put(clazz, converter);
    }

    @Override
    public void register(Class<?> clazz, Class<? extends Converter<?>> converterClass) {
        Converter<?> converter = DependProvider.getInstance().newInstance(converterClass);
        register(clazz, converter);
    }

    @Override
    public void clear() {
        converters.clear();
    }

    @Override
    public <T> Converter<T> getConverter(Class<T> clazz) {
        Converter<T> converter = (Converter<T>)converters.get(clazz);
        if(converter==null) {
            converter = new ConverterImpl(clazz);
        }
        return converter;
    }
}
