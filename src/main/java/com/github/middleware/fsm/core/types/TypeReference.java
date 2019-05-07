package com.github.middleware.fsm.core.types;

import com.github.middleware.fsm.exception.StateMachineException;

import java.lang.reflect.*;

/**
 * 处理Super Type Tokens
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public abstract class TypeReference<T> {
    private Type type;

    protected TypeReference() {
        type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    public final Class<T> getOrgType() {
        // raw type is |T|
        Class<T> result = (Class<T>) getOrgType(type);
        return result;
    }

    static Class<?> getOrgType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // JDK implementation declares getRawType() to return Class<?>
            return (Class<?>) parameterizedType.getRawType();
        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            return Array.newInstance(getOrgType(genericArrayType.getGenericComponentType()), 0).getClass();
        } else if (type instanceof TypeVariable) {
            // First bound is always the "primary" bound that determines the
            // runtime signature.
            return getOrgType(((TypeVariable<?>) type).getBounds()[0]);
        } else if (type instanceof WildcardType) {
            // Wildcard can have one and only one upper bound.
            return getOrgType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            throw new StateMachineException(type + " unsupported");
        }
    }

}
