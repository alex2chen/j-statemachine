package com.github.middleware.fsm.core.factory;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用线程池
 * 场景：事件监听触发、执行Action
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/8
 */
public class ThreadExecutorService {
    public static ExecutorService getExecutor() {
        ExecutorService executorService = SingletonProviderFactory.getInstance().get(ExecutorService.class);
        if (executorService == null) {
            executorService = registerNewExecutorService(1, 120, TimeUnit.SECONDS);
        }
        return executorService;
    }

    private static ExecutorService registerNewExecutorService(final int threadNum,
                                                              final long terminationTimeout, final TimeUnit timeUnit) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        MoreExecutors.addDelayedShutdownHook(executorService, terminationTimeout, timeUnit);
        SingletonProviderFactory.getInstance().register(ExecutorService.class, executorService);
        return executorService;
    }
}
