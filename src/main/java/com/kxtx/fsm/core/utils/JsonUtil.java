package com.kxtx.fsm.core.utils;

import com.alibaba.fastjson.JSON;

/**
 * Created by fei.chen on 2017/9/11.
 */
public class JsonUtil {

    public static String json(Object input) {
        try {
            return JSON.toJSONString(input);
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }
}
