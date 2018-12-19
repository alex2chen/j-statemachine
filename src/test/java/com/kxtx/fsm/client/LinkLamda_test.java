package com.kxtx.fsm.client;

import com.google.common.base.Stopwatch;
import com.kxtx.fsm.builder.event.TransitionBeginEvent;
import com.kxtx.fsm.builder.event.TransitionBeginListener;
import com.kxtx.fsm.builder.plugin.log.LogExec;
import com.kxtx.fsm.filter.annotation.PostConstruct;
import com.kxtx.fsm.builder.machine.factroy.StateMachineBuilderFactory;
import com.kxtx.fsm.builder.machine.support.visit.AbstractStateMachineProxy;
import com.kxtx.fsm.builder.machine.support.visit.StateMachineProxy;
import com.kxtx.fsm.builder.machine.support.visit.StateMachineBuilderProxy;
import com.kxtx.fsm.config.annotation.StateMachineParameters;
import com.kxtx.fsm.exception.StateMachineException;
import com.kxtx.fsm.filter.validator.PreChecker;
import com.kxtx.fsm.filter.wrapper.PostProcessor;
import com.kxtx.fsm.filter.wrapper.PostProcessorProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/12
 */
public class LinkLamda_test {
    enum Event1 {
        ToA,
        ToB,
        ToC
    }

    @StateMachineParameters(stateType = String.class, eventType = Event1.class, contextType = Integer.class)
    static class StateMachineCase extends AbstractStateMachineProxy {
        @LogExec
        protected void fromAToB(String from, String to, Event1 event, Integer context) {
            System.out.println("from '" + from + "' to '" + to + "' on event '" + event + "' with context '" + context + "'.");
        }

        @LogExec
        protected void exitA(String from, String to, Event1 event, Integer context) {
            System.out.println("exit State：" + from);
        }

        @PostConstruct
        protected void postConstruct(Event1 ex1, String ex2) {
            System.out.println("postConstruct ex1:" + ex1 + ",ex2:" + ex2);
        }

        protected void entryB(String from, String to, Event1 event, Integer context) {
            System.out.println("entry State：" + to);
        }
    }

    private StateMachineBuilderProxy builder = StateMachineBuilderFactory.create(StateMachineCase.class, new Class[]{Event1.class, String.class});
    private Stopwatch stopwatch;

    @Before
    public void beforeMethod() {
        stopwatch = Stopwatch.createStarted();
    }

    @Test
    public void go_handleEvent() {
        builder.localTransition().from("A").to("B").on(Event1.ToB)
                .when(new PreChecker<Object>() {
                    @Override
                    public boolean validate(Object context) throws StateMachineException {
                        if (context instanceof Integer) {
                            int contextParam = (Integer) context;
                            if (contextParam % 2 == 0) return true;
                        }
                        return false;
                    }

                    @Override
                    public String getName() {
                        return "PreChecker";
                    }
                }).complete("fromAToB");
        builder.onExit("A").complete("exitA");
        builder.onEntry("B").complete("entryB");

        StateMachineProxy fsm = builder.newStateMachine("A");
        fsm.handleEvent(Event1.ToB, 10);
        //fsm.handleEvent(Event1.ToB, 9);
        //System.out.println(fsm.getLastException());
        Assert.assertThat(fsm.getCurrentState(), is("B"));
        System.out.println("times:" + stopwatch);
    }

    @Test()
    public void go_handleEvent_whenMvEl() {
        builder.localTransition().from("A").to("B").on(Event1.ToB).whenMvel("context% 2 == 0").complete("fromAToB");
        StateMachineProxy fsm = builder.newStateMachine("A");
        //fsm.handleEvent(Event1.ToB, 9);
        fsm.handleEvent(Event1.ToB, 8);
        Assert.assertThat(fsm.getCurrentState(), is("B"));
        System.out.println("times:" + stopwatch);
    }

    @Test
    public void go_handleEvent_postConstruct() {
        builder.localTransition().from("A").to("B").on(Event1.ToB).complete("fromAToB");
        StateMachineProxy fsm = builder.newStateMachine("A", Event1.ToB, "abc");
        fsm.handleEvent(Event1.ToB, 10);
        System.out.println("当前状态:" + fsm.getCurrentState());
        System.out.println("times:" + stopwatch);
    }

    @Test
    public void go_handleEvent_PostProcessor() {
        PostProcessor<StateMachineCase> p1 = new PostProcessor<StateMachineCase>() {
            @Override
            public void postProcess(StateMachineCase obj) {
                System.out.println("PostProcessor:" + obj.getStates());
            }
        };
        PostProcessorProvider.getInstance().register(StateMachineCase.class, p1);

        builder.localTransition().from("A").to("B").on(Event1.ToB).complete("fromAToB");
        StateMachineProxy fsm = builder.newStateMachine("A");
        fsm.handleEvent(Event1.ToB, 10);
        System.out.println("当前状态:" + fsm.getCurrentState());
        System.out.println("times:" + stopwatch);
    }

    @Test
    public void go_handleEvent_Listener() {
        builder.localTransition().from("A").to("B").on(Event1.ToB).complete("fromAToB");
        StateMachineProxy fsm = builder.newStateMachine("A");
        fsm.addTransitionBeginListener(new TransitionBeginListener<StateMachineProxy, Object, Object, Object>() {
            @Override
            public void transitionBegin(TransitionBeginEvent<StateMachineProxy, Object, Object, Object> event) {
                System.out.println("transitionBegin....");
            }
        });
        fsm.handleEvent(Event1.ToB, 10);
        System.out.println("当前状态:" + fsm.getCurrentState());
        System.out.println("times:" + stopwatch);
    }


}
