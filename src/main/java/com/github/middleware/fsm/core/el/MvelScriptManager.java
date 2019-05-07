package com.github.middleware.fsm.core.el;

/**
 * Created by YT on 2017/3/7.
 */
public interface MvelScriptManager {
    public static final String SEPARATOR_CHARS = "::";
    public static final String VAR_CONTEXT = "context";

    <T> T eval(String script, Object context, Class<T> returnType);

    void compile(String script);

    boolean evalBoolean(String script, Object context);
}
