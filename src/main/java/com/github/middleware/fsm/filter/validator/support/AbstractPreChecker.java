package com.github.middleware.fsm.filter.validator.support;

import com.github.middleware.fsm.filter.validator.PreChecker;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/3
 */
public abstract class AbstractPreChecker<C> implements PreChecker<C> {
    @Override
    public String getName() {
        return getClass().getSimpleName() + "_Checker";
    }
}
