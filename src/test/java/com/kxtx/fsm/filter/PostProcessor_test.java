package com.kxtx.fsm.filter;


import com.kxtx.fsm.core.Swith;
import com.kxtx.fsm.core.factory.DependProvider;
import com.kxtx.fsm.filter.wrapper.PostProcessor;
import com.kxtx.fsm.filter.wrapper.PostProcessorProvider;
import com.kxtx.fsm.filter.wrapper.support.PostConstructPostProcessorImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/12
 */
public class PostProcessor_test {
    @Test
    public void go_register() {
        PostProcessorProvider.getInstance().register(Swith.class, new PostProcessor<Swith>() {
            @Override
            public void postProcess(Swith Obj) {
                Obj.setName("alex");
            }
        });
        Swith swith = DependProvider.getInstance().newInstance(Swith.class);
        Assert.assertThat(swith, notNullValue());
        assertThat(swith.getName(), equalTo("alex"));
    }

    @Test
    public void go_getCallablePostProcessors() {
        PostProcessor<Object> postProcessor = new PostProcessor<Object>() {
            @Override
            public void postProcess(Object Obj) {
            }
        };
        PostProcessorProvider.getInstance().register(Swith.class, postProcessor);
        List<PostProcessor<? super Swith>> postProcessors = PostProcessorProvider.getInstance().getProcessors(Swith.class);
        Assert.assertThat((PostProcessor<Object>) postProcessors.get(0), sameInstance(postProcessor));

        PostProcessorProvider.getInstance().unRegister(Swith.class);
        postProcessors = PostProcessorProvider.getInstance().getProcessors(Swith.class);
        Assert.assertThat(postProcessors, empty());
    }

    @Test
    public void testCompositePostProssor() {
        PostProcessor<Swith> p1 = new PostProcessor<Swith>() {
            @Override
            public void postProcess(Swith p) {
                p.setName("Alex");
            }
        };
        PostProcessor<Swith> p2 = new PostProcessor<Swith>() {
            @Override
            public void postProcess(Swith p) {
                p.setVersion("0.1");
            }
        };
        PostProcessorProvider.getInstance().register(Swith.class, p1);
        PostProcessorProvider.getInstance().register(Swith.class, p2);
        List<PostProcessor<? super Swith>> studentPostProcessors = PostProcessorProvider.getInstance().getProcessors(Swith.class);
        assertThat(studentPostProcessors.size(), is(1));
        Swith student = DependProvider.getInstance().newInstance(Swith.class);
        assertThat(student.getName(), equalTo("Alex"));
        assertThat(student.getVersion(), equalTo("0.1"));
    }

    @Test
    public void go_postConstruct() {
        PostProcessorProvider.getInstance().register(Swith.class, PostConstructPostProcessorImpl.class);
        Swith p = DependProvider.getInstance().newInstance(Swith.class);
        assertThat(p.getName(), equalTo(".."));
        assertThat(p.getVersion(), equalTo("0.0"));
    }


}
