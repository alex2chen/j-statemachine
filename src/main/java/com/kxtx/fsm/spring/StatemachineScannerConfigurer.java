package com.kxtx.fsm.spring;

import com.kxtx.fsm.spring.annotation.StatemachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by netuser on 17-6-5.
 */
public class StatemachineScannerConfigurer implements InitializingBean, ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(StatemachineScannerConfigurer.class);
    private ApplicationContext applicationContext;
    private String basePackages;
    private Class<? extends Annotation> annotationClz = StatemachineService.class;

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    public Class<? extends Annotation> getAnnotationClz() {
        return annotationClz;
    }

    public void setAnnotationClz(Class<? extends Annotation> annotationClz) {
        this.annotationClz = annotationClz;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Scanner scanner = new Scanner(beanDefinitionRegistry);
        scanner.setResourceLoader(this.applicationContext);
        scanner.scan(this.basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(this.basePackages, "Property basePackages is not empty.");
        Assert.notNull(this.annotationClz, "Property annotationClz is required.");
        Assert.notNull(this.applicationContext, "Property applicationContext is required.");
    }

    private final class Scanner extends ClassPathBeanDefinitionScanner {
        public Scanner(BeanDefinitionRegistry registry) {
            super(registry);
        }

        @Override
        protected void registerDefaultFilters() {
            this.addIncludeFilter(new AnnotationTypeFilter(getAnnotationClz()));
        }

        @Override
        protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
            Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
            if (beanDefinitionHolders.isEmpty()) {
                LOG.warn("statemachine class not found in " + basePackages);
            } else {
                Iterator<BeanDefinitionHolder> items = beanDefinitionHolders.iterator();
                while (items.hasNext()) {
                    BeanDefinitionHolder beanHolder = items.next();
                    GenericBeanDefinition genericBean = (GenericBeanDefinition) beanHolder.getBeanDefinition();
                    genericBean.getPropertyValues().addPropertyValue("statemachineImplClz", genericBean.getBeanClassName());
                    genericBean.getPropertyValues().addPropertyValue("applicationContext", StatemachineScannerConfigurer.this.applicationContext);
                    genericBean.setBeanClass(StatemachineFactoryBean.class);
                }
            }
            return beanDefinitionHolders;
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata().hasAnnotation(getAnnotationClz().getName());
        }
    }
}
