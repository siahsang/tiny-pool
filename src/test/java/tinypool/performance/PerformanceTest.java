package tinypool.performance;

import org.junit.Test;
import tinypool.ObjectPool;
import tinypool.ObjectPoolImpl;
import tinypool.StringBuilderObjectFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class PerformanceTest {
    private final int WORKER_COUNT = 500;
    private final int WORKER_OPERATION = 100;

    @Test
    public void testPerformanceTinyPool() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(WORKER_COUNT);

        Worker[] tinyPoolWorkers = new TinyPoolWorker[WORKER_COUNT];
        ObjectPool<StringBuilder> pool = new ObjectPoolImpl<>(5, 16, new StringBuilderObjectFactory());

        for (int i = 0; i < WORKER_COUNT; i++) {
            tinyPoolWorkers[i] = new TinyPoolWorker(countDownLatch, WORKER_OPERATION, pool);
            new Thread(tinyPoolWorkers[i]).start();
        }

        countDownLatch.await();

        printStatistics("TinyPool", tinyPoolWorkers);


    }

    private void printStatistics(String poolName, Worker[] workers) {
        long totalTimeMs = 0;
        long totalErrorCounts = 0;

        for (Worker worker : workers) {
            totalTimeMs += worker.getTotalRunningTimeMS();
            totalErrorCounts += worker.getErrorCount();
        }

        final double averageThroughput = (double) totalTimeMs / (WORKER_COUNT * WORKER_OPERATION);
        final long errorRatio = totalErrorCounts / (WORKER_COUNT * WORKER_OPERATION);

        System.out.println("Statistics for " + poolName + " --------------------- ");

        System.out.println("Error ration " + errorRatio);
        System.out.println("Average throughput per MS " + averageThroughput);

    }


}
