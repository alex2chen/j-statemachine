package com.github.middleware.fsm.core;

import com.github.middleware.fsm.core.factory.DependProvider;
import com.github.middleware.fsm.core.support.SwithImpl;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/12
 */
public class DependProvider_test {
    @Test
    public void go_newInstance() {
        Swith swith = DependProvider.getInstance().newInstance(Swith.class);
        Assert.assertThat(swith, notNullValue());

        swith = DependProvider.getInstance().newInstance(SwithImpl.class);
        Assert.assertThat(swith, instanceOf(SwithImpl.class));
    }

    @Test
    public void go_newInstanc_forArg(){
        Swith swith = DependProvider.getInstance().newInstance(Swith.class,new Class[]{String.class}, new Object[]{"Alex"});
        Assert.assertThat(swith,notNullValue());
        Assert.assertThat(swith.getName(),equalTo("Alex"));
    }
}
