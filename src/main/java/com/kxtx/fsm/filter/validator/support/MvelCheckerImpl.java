package com.kxtx.fsm.filter.validator.support;

import com.google.common.collect.Maps;
import com.kxtx.fsm.core.el.MvelScriptManager;
import com.kxtx.fsm.filter.validator.PreChecker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class MvelCheckerImpl<C> implements PreChecker<C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MvelCheckerImpl.class);
    private String mvelExpr;
    private String name;
    private MvelScriptManager scriptManager;
    private String script;

    public MvelCheckerImpl(String script, MvelScriptManager scriptManager) {
        String[] arrays = StringUtils.split(script, MvelScriptManager.SEPARATOR_CHARS);
        if (arrays.length == 2) {
            this.name = arrays[0].trim();
            this.mvelExpr = arrays[1].trim();
        } else {
            this.name = "_NoName_";
            this.mvelExpr = arrays[0].trim();
        }

        this.script = script;
        this.scriptManager = scriptManager;

        scriptManager.compile(mvelExpr);
    }

    @Override
    public boolean validate(C context) {
        try {
            Map<String, Object> variables = Maps.newHashMap();
            variables.put(MvelScriptManager.VAR_CONTEXT, context);
            return scriptManager.evalBoolean(mvelExpr, variables);
        } catch (Exception e) {
            LOGGER.error("Evaluate \"" + mvelExpr + "\" failed with " + e.getMessage() + (e.getCause() != null ? ", which caused by " + e.getCause().getMessage() : ""));
            return false;
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
