package com.github.middleware.fsm.builder.exec.support;

import com.github.middleware.fsm.builder.exec.dto.RuleExecContext;
import com.github.middleware.fsm.builder.machine.support.AbstractStateMachine;
import com.github.middleware.fsm.builder.StateMachine;
import com.github.middleware.fsm.builder.StateMachines;
import com.github.middleware.fsm.core.utils.Precondition;
import com.github.middleware.fsm.core.utils.ReflectUtil;
import com.github.middleware.fsm.builder.exec.Action;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 方法调用代理
 * TODO:根据方法名支持更多执行策略,比如：ignore/before/after
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/7
 */
public class MethodCallProxyImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements Action<T, S, E, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodCallProxyImpl.class);
    private String methodName;
    private RuleExecContext execContext;
    private Action<T, S, E, C> delegator;
    private int weight;
    private long timeout;

    /**
     * @param methodName
     * @param execContext
     */
    public MethodCallProxyImpl(String methodName, RuleExecContext execContext) {
        this.methodName = methodName;
        this.execContext = execContext;
        this.weight = Action.WEIGHT_NORMAL;
    }

    @Override
    public void execute(S from, S to, E event, C context, T stateMachine,String actionName) {
        Precondition.checkNotNull(stateMachine, "校验异常：状态机实现类不能为空.");
        getDelegator().execute(from, to, event, context, stateMachine, actionName);
    }

    private Action<T, S, E, C> getDelegator() {
        if (delegator == null) {
            Method method = ReflectUtil.searchMethod(execContext.getExecutionTargetType(), AbstractStateMachine.class, methodName, execContext.getMethodCallParamTypes());
            if (method != null) {
                delegator = StateMachines.newMethodCallAction(method, weight, execContext);
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("未找到方法 '" + methodName + "' 参数 '[" + StringUtils.join(execContext.getMethodCallParamTypes(), ", ") + "]' in class " + execContext.getExecutionTargetType() + ".");
                }
                delegator = (Action<T, S, E, C>) Action.DUMMY_ACTION;
            }
        }
        return delegator;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public int weight() {
        return this.weight;
    }

    @Override
    public long timeout() {
        return this.timeout;
    }

    @Override
    public boolean isAsync() {
        return false;
    }


}
