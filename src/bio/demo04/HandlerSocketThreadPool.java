package bio.demo04;

import java.util.concurrent.*;

/**
 * 线程池处理客户端连接
 * @author zhangling  2021/5/12 19:22
 */
public class HandlerSocketThreadPool {

    private ExecutorService executor;

    /**
     *
     * @param maxPoolSize 最大线程数
     * @param queueSize 任务队列任务数量
     */
    public HandlerSocketThreadPool(int maxPoolSize, int queueSize) {
        this.executor = new ThreadPoolExecutor(3,
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task) {
        // 线程池执行线程
        this.executor.execute(task);
    }
}
