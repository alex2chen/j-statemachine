package com.kxtx.fsm.filter.validator;

import com.kxtx.fsm.exception.StateMachineException;

/**
 * 检查器，在状态扭转链前被执行
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public interface PreChecker<C> {
    boolean validate(C context) throws StateMachineException;

    String getName();

}
