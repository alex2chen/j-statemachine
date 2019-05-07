package com.github.middleware.fsm.spring;

import com.github.middleware.fsm.builder.machine.factroy.StateMachineBuilderFactory;
import com.github.middleware.fsm.builder.machine.support.visit.AbstractStateMachineProxy;
import com.github.middleware.fsm.builder.machine.support.visit.StateMachineBuilderProxy;
import com.github.middleware.fsm.filter.wrapper.PostProcessor;
import com.github.middleware.fsm.filter.wrapper.PostProcessorProvider;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * Created by netuser on 17-6-5.
 */
public class StatemachineFactoryBean<T> implements InitializingBean, FactoryBean<T> {
    private Class statemachineImplClz;
    private ApplicationContext applicationContext;

    @Override
    public T getObject() throws Exception {
        if (AbstractStateMachineProxy.class.isAssignableFrom(statemachineImplClz)) {
            System.out.println();
            StateMachineBuilderProxy builderProxy = StateMachineBuilderFactory.create(statemachineImplClz);
            //builderProxy.newPluginBuilder(new FsmListenerLogger()).install();
            PostProcessor<T> postProcessor = new PostProcessor<T>() {
                @Override
                public void postProcess(T obj) {
                    AutowireCapableBeanFactory capableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                    capableBeanFactory.autowireBean(obj);
                }
            };
            PostProcessorProvider.getInstance().register(statemachineImplClz, postProcessor);
            return (T) builderProxy.newStateMachine();
        }
        throw new Exception("no such statemachineFactoryBean.");
    }

    @Override
    public Class<?> getObjectType() {
        return statemachineImplClz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.statemachineImplClz, "Property statemachineImplClz is required.");
        Assert.notNull(this.applicationContext, "Property applicationContext is required.");
    }

    public Class getStatemachineImplClz() {
        return statemachineImplClz;
    }

    public void setStatemachineImplClz(Class statemachineImplClz) {
        this.statemachineImplClz = statemachineImplClz;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
