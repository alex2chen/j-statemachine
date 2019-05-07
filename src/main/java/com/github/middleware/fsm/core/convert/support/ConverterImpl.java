package com.github.middleware.fsm.core.convert.support;

import com.github.middleware.fsm.core.convert.Converter;
import com.github.middleware.fsm.exception.StateMachineException;
import org.apache.commons.lang3.StringUtils;

import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

/**
 * S,E 赞只支持String,Enum
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class ConverterImpl<T> implements Converter<T> {
    private Class<T> type;

    public ConverterImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public String objToString(T obj) {
        if (Enum.class.isAssignableFrom(type)) {
            return ((Enum<?>) obj).name();
        }
        return obj != null ? obj.toString() : StringUtils.EMPTY;
    }

    @Override
    public T stringToObj(String name) {
        try {
            if (String.class.equals(type)) {
                return type.cast(value);
            }
            if (Enum.class.isAssignableFrom(type)) {
                return (T) type.cast(Enum.valueOf((Class) type, name));
            }
        } catch (Exception ex) {
            throw new StateMachineException("类型转换异常: " + type.getName() + " : " + name);
        }
        throw new StateMachineException("未知类型转换异常: " + type.getName() + " : " + name);
    }
}
