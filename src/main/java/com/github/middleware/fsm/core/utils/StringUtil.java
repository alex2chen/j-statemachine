package com.github.middleware.fsm.core.utils;

import com.github.middleware.fsm.exception.StateMachineException;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class StringUtil {
    /**
     * 根据classname截取
     *
     * @param className
     * @return
     */
    public static String getNameByClassName(final String className) {
        if (className == null || className.length() == 0)
            throw new StateMachineException("校验异常：参数不能为空");
        int index = className.lastIndexOf('.');
        if (index != -1)
            return className.substring(0, index);
        return "";
    }
}
