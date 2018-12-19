package com.kxtx.fsm.builder.exec.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.core.utils.ReflectUtil;
import com.kxtx.fsm.builder.exec.Action;
import com.kxtx.fsm.builder.exec.dto.RuleExecContext;
import com.kxtx.fsm.builder.plugin.log.LogExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 方法调用
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class MethodCallImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements Action<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodCallImpl.class);
    private Method method;
    private boolean logExec;
    private String scriptExpr;
    private int weight;
    @JSONField(serialize = false)
    private RuleExecContext execContext;
    private String serviceName;

    public MethodCallImpl(Method method, int weight, RuleExecContext execContext) {
        this.method = method;
        this.weight = Action.WEIGHT_NORMAL;
        this.execContext = execContext;
        if (method != null) {
            LogExec logExecObj = method.getAnnotation(LogExec.class);
            if (logExecObj != null) {
                if (!Strings.isNullOrEmpty(logExecObj.when())) {
                    logExec = true;
                    scriptExpr = logExecObj.when();
                    this.execContext.getScriptManager().compile(scriptExpr);
                }
            }
        }
    }

    @Override
    public void execute(S from, S to, E event, C context, T stateMachine, String serviceName) {
        this.serviceName = serviceName;
        Object[] paramValues = Lists.newArrayList(from, to, event, context).subList(0, execContext.getMethodCallParamTypes().length).toArray();
        // LOGGER.info("fsm.fire exec start,method:" + methodToString(this.method) + ",scriptExpr:" + scriptExpr + ",paramValues:" + paramValues);
        if (scriptExpr != null) {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("context", context);
            boolean isAllowed = execContext.getScriptManager().evalBoolean(scriptExpr, variables);
            if (!isAllowed) return;
        }
        if (logExec) {
            Stopwatch sw = Stopwatch.createStarted();
            ReflectUtil.invoke(method, stateMachine, paramValues);
            LOGGER.info("fsm.fire exec," + getName() + " times:" + sw);
        } else {
            ReflectUtil.invoke(method, stateMachine, paramValues);
        }
    }

    @Override
    public String getName() {
        return serviceName + "." + method;
    }

    @Override
    public int weight() {
        return 0;
    }

    @Override
    public long timeout() {
        return 0;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    private String methodToString(Method method) {
        if (method == null)
            return null;
        StringBuilder builder = new StringBuilder(method.getDeclaringClass().getSimpleName());
        builder.append('.').append(method.getName()).append('(');
        if (method.getParameterTypes() != null) {
            for (int i = 0, size = method.getParameterTypes().length; i < size; ++i) {
                if (i != 0) builder.append(", ");
                builder.append(method.getParameterTypes()[i].getSimpleName());
            }
        }
        builder.append(')');
        return builder.toString();
    }

    @Override
    public String toString() {
        return "MethodCallImpl{" +
                "method=" + method +
                ", logExec=" + logExec +
                ", scriptExpr='" + scriptExpr + '\'' +
                ", weight=" + weight +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
