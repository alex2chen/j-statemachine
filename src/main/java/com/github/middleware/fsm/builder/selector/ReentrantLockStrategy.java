package com.github.middleware.fsm.builder.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 非线程安全模式下（排他锁策略）
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
@Deprecated
public class ReentrantLockStrategy{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReentrantLockStrategy.class);
    private ReentrantLock lock = new ReentrantLock();

    public ReentrantLockStrategy() {
    }

//    @Override
//    public void handleEvent(E event, Dispatcher dispatcher) {
//        try {
//            lock.tryLock(0, TimeUnit.SECONDS);
//            List<TransitionDefine<T, S, E, C>> transitions = dispatcher.getCurrentState().getTransitions();
//            for (TransitionDefine<T, S, E, C> transition : transitions) {
//                if (!isPassAllChecker(transition, event, dispatcher.getRequestPayload())) {
//                    continue;
//                }
//                AbstractState<T, S, E, C> newState = transition.getToState();
//                LOGGER.info("状态扭转链：" + dispatcher.getCurrentState().getName() + " => " + newState.getName() + " exec");
//                doActions(newState, transition.getActionList(), dispatcher);
//                dispatcher.setCurrentState(newState);
//                isFired = true;
//                break;
//            }
//        } catch (InterruptedException ie) {
//            LOGGER.warn("handleEvent: interrupted exception");
//        } finally {
//            lock.unlock();
//        }
//    }
}
