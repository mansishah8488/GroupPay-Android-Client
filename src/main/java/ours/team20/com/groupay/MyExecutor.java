package ours.team20.com.groupay;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ken on 5/4/2015.
 */
public class MyExecutor {
    static int corePoolSize = 60;
    static int maximumPoolSize = 80;
    static int keepAliveTime = 10;

    static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    static final Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    public static Executor getExecutor(){
        return threadPoolExecutor;
    }
}
