package com.mxjlife.taobaoke.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description: 线程池配置
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/7/23 17:09
 */
@Slf4j
public class ThreadPoolUtils {

//    @Value("${system.threadPool.corePoolSizen}")
//    int corePoolSizen;
//    @Value("${system.threadPool.maximumPoolSize}")
//    int maximumPoolSize;
//    @Value("${system.threadPool.keepAliveTime}")
//    long keepAliveTime;


    private static final int corePoolSizen = 10;
    private static final int maximumPoolSize = 20;
    private static final long keepAliveTime = 30;

    private static volatile ThreadPoolExecutor threadPool;

    private ThreadPoolUtils() {
    }

    public static ThreadPoolExecutor getThreadPool(){
        if(threadPool == null){
            synchronized (ThreadPoolUtils.class){
                if(threadPool == null){
                    threadPool = new ThreadPoolExecutor(corePoolSizen, maximumPoolSize,
                            keepAliveTime, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(1000),
                            new MyThreadFactory(),
                            new MyAbortPolicy());
                    log.info("initialization threadPool success");
                }
            }
        }
        return threadPool;
    }

    public static void execute(Runnable r){
        getThreadPool().execute(r);
    }


    /**
     * 线程工厂
     */
    static class MyThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "threadPool-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            if(log.isDebugEnabled()){
                log.debug("threadPool create new thread -> {}", t.getName());
            }

            return t;
        }
    }

    /**
     * 自定义线程池拒绝策略
     */
    public static class MyAbortPolicy implements RejectedExecutionHandler {
        MyAbortPolicy() {
        }
        /**
         * 重写线程池拒绝策略
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.error("线程池拒绝请求 ->{}", r);
        }
    }

}