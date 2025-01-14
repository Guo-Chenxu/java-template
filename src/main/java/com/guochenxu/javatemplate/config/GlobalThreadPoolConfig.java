package com.guochenxu.javatemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class GlobalThreadPoolConfig {

    // 定义线程池参数
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final long KEEP_ALIVE_TIME = 1L;
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<>();
    private static final ThreadFactory THREAD_FACTORY = Executors.defaultThreadFactory();
    private static final RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.AbortPolicy();

    @Bean(name = "globalThreadPool")
    public ThreadPoolExecutor globalThreadPool() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, WORK_QUEUE, THREAD_FACTORY, HANDLER);
    }
}