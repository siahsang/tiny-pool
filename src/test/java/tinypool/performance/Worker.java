package tinypool.performance;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public abstract class Worker implements Runnable {
    private final CountDownLatch countDownLatch;
    protected final long loop;


    protected int errorCount = 0;
    protected long totalRunningTimeMS;


    public Worker(CountDownLatch countDownLatch, long loop) {
        this.countDownLatch = countDownLatch;
        this.loop = loop;
    }

    @Override
    public void run() {
        Instant start = Instant.now();

        doWork();

        Instant finish = Instant.now();
        totalRunningTimeMS = Duration.between(start, finish).toMillis();

        countDownLatch.countDown();

    }

    public int getErrorCount() {
        return errorCount;
    }

    public long getTotalRunningTimeMS() {
        return totalRunningTimeMS;
    }


    public abstract void doWork();

}
