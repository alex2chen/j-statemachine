package com.github.middleware.fsm.builder;

import com.github.middleware.fsm.builder.event.annotation.OnAfterAction;
import com.github.middleware.fsm.builder.event.annotation.OnTransitionBegin;
import com.github.middleware.fsm.builder.event.dto.ListenerMappingContext;
import com.github.middleware.fsm.builder.exec.Action;
import com.github.middleware.fsm.config.annotation.State;
import com.github.middleware.fsm.config.annotation.Transition;
import com.github.middleware.fsm.config.annotation.Transitions;
import com.github.middleware.fsm.config.loader.AnnotationResovler;
import com.github.middleware.fsm.core.factory.DependProvider;
import com.github.middleware.fsm.builder.plugin.support.AbstractPluginProvider;
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
