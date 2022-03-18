package com.cqc.tank.config;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Cqc
 * @date 2022/3/17
 */
public class ThreadPoolConfig {
    private static volatile ThreadPoolExecutor executor;
    private static int corePoolSize = 5;
    private static int maximumPoolSize = 30;
    /**
     * 空闲线程活跃时间
     */
    private static long keepAliveTime = 60;
    private static TimeUnit unit = TimeUnit.SECONDS;
    /**
     * 线程池，队列容量
     */
    private static int queueCapacity = 5;

    private static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            synchronized (ThreadPoolConfig.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                            new LinkedBlockingDeque<>(queueCapacity), new ThreadPoolExecutor.DiscardPolicy());
                }
            }
        }
        return executor;
    }

    /**
     * 线程池执行一个任务
     * @param task
     */
    public static void execute(Runnable task) {
        getExecutor().execute(task);
    }

}
