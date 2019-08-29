package tinypool.performance;

import org.junit.Test;
import org.vibur.objectpool.ConcurrentPool;
import org.vibur.objectpool.PoolService;
import org.vibur.objectpool.util.ConcurrentLinkedDequeCollection;
import tinypool.ObjectPool;
import tinypool.ObjectPoolImpl;
import tinypool.StringBuilderObjectFactory;
import tinypool.performance.vibur.ViburObjectFactory;
import tinypool.performance.vibur.ViburObjectWorker;

import java.util.concurrent.CountDownLatch;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class PerformanceTest {
    private final int WORKER_COUNT = 50;
    private final int WORKER_OPERATION = 1000;
    private final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(WORKER_COUNT);


    @Test
    public void testPerformanceTinyPool() throws Exception {

        Worker[] tinyPoolWorkers = new TinyPoolWorker[WORKER_COUNT];
        ObjectPool<StringBuilder> pool = new ObjectPoolImpl<>(50, 50, new StringBuilderObjectFactory());

        for (int i = 0; i < WORKER_COUNT; i++) {
            tinyPoolWorkers[i] = new TinyPoolWorker(COUNT_DOWN_LATCH, WORKER_OPERATION, pool);
            new Thread(tinyPoolWorkers[i]).start();
        }

        COUNT_DOWN_LATCH.await();

        printStatistics("TinyPool", tinyPoolWorkers);
    }

    @Test
    public void testPerformanceViburObjectPool() throws Exception {
        Worker[] tinyPoolWorkers = new ViburObjectWorker[WORKER_COUNT];
        PoolService<StringBuilder> pool = new ConcurrentPool<>(new ConcurrentLinkedDequeCollection<>(),
                new ViburObjectFactory(), 50, 50, true, null);

        for (int i = 0; i < WORKER_COUNT; i++) {
            tinyPoolWorkers[i] = new ViburObjectWorker(COUNT_DOWN_LATCH, WORKER_OPERATION, pool);
            new Thread(tinyPoolWorkers[i]).start();
        }

        COUNT_DOWN_LATCH.await();

        printStatistics("Vibur Object Pool", tinyPoolWorkers);
    }


    private void printStatistics(final String poolName, Worker[] workers) {
        long totalTimeMs = 0;
        long totalErrorCounts = 0;

        for (Worker worker : workers) {
            totalTimeMs += worker.getTotalRunningTimeMS();
            totalErrorCounts += worker.getErrorCount();
        }

        final long errorRatio = totalErrorCounts / (WORKER_COUNT * WORKER_OPERATION);

        System.out.println("Statistics for " + poolName + " --------------------- ");

        System.out.println("Error ration " + errorRatio);
        System.out.println("Total execution time ms " + totalTimeMs);

    }


}
