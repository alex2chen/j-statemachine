package com.github.middleware.fsm.core.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
public class ProcessingQueue<E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingQueue.class);
    public static final int MAX_EVENT_QUEUE_SIZE = 30;
    private Queue<E> eventQueue = new ArrayBlockingQueue<E>(MAX_EVENT_QUEUE_SIZE);
    //全局事件
    private List<E> globalEvents = new ArrayList<E>();
    private E currentEvent;

    /**
     * add event to queue
     *
     * @param newEvent
     */
    public void addEvent(E newEvent) {
        if (newEvent == null)
            return;
        eventQueue.offer(newEvent);
    }

    /**
     * get a event
     *
     * @return
     */
    public E nextEvent() {
        currentEvent = eventQueue.poll();
        return currentEvent;
    }

    /**
     * @param resultEvent
     */
    @Deprecated
    public void addGlobalEvent(E resultEvent) {
        if (resultEvent != null) {
            globalEvents.add(resultEvent);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("addGlobalEvent(" + resultEvent + ")");
            }
        }
    }

    public E currentEvent() {
        return currentEvent;
    }

    public List<E> getGlobalEvents() {
        return this.globalEvents;
    }

    @Deprecated
    public void clearGlobalEvents() {
        globalEvents.clear();
    }

    public boolean hasEvent() {
        return eventQueue.size() > 0;
    }

    public boolean hasGlobalEvents() {
        return !globalEvents.isEmpty();
    }
}
