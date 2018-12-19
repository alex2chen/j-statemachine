package com.kxtx.fsm.builder;

import com.kxtx.fsm.builder.event.annotation.OnAfterAction;
import com.kxtx.fsm.builder.event.annotation.OnTransitionBegin;
import com.kxtx.fsm.builder.event.dto.ListenerMappingContext;
import com.kxtx.fsm.builder.exec.Action;
import com.kxtx.fsm.builder.plugin.support.AbstractPluginProvider;
import com.kxtx.fsm.config.annotation.State;
import com.kxtx.fsm.config.annotation.Transition;
import com.kxtx.fsm.config.annotation.Transitions;
import com.kxtx.fsm.config.loader.AnnotationResovler;
import com.kxtx.fsm.core.factory.DependProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/17
 */
@State(name = "Test", alias = "_test")
@Transitions({@Transition(from = "SH", to = "BJ", on = "火车"), @Transition(from = "BJ", to = "WH", on = "飞机")})
public class AnnotationResovler_test {
    private AnnotationResovler annotationResovler = DependProvider.getInstance().newInstance(AnnotationResovler.class);

    @Test
    public void go_processStateLoader() {
        List<State> states = annotationResovler.processStateLoader(AnnotationResovler_test.class);
        Assert.assertThat(states.size(), is(1));
    }

    @Test
    public void go_processTransitionLoader() {
        List<Transition> transitions = annotationResovler.processTransitionLoader(AnnotationResovler_test.class);
        System.out.println(transitions);
        Assert.assertThat(transitions.size(), is(2));
    }

    @Test
    public void go_processEventListener() {
        ListenerMappingContext listenerMappingContext = annotationResovler.processEventListener(new MonitorPlugin());
        System.out.println(listenerMappingContext);
        Assert.assertThat(listenerMappingContext.getMappingContextItems().size(), is(2));
    }

    public class MonitorPlugin extends AbstractPluginProvider {
        @OnTransitionBegin(when = "1==1")
        public void onTransB(Object from, Object event, Object context) {

        }

        @OnAfterAction
        public void onExexAfter(Object sourceState, Object targetState,
                                Object event, Object context, int[] mOfN, Action<?, ?, ?, ?> action) {

        }
    }
}
