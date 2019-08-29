package tinypool.performance.vibur;

import org.vibur.objectpool.PoolService;
import tinypool.performance.Worker;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class ViburObjectWorker extends Worker {

    private final PoolService<StringBuilder> viburPool;

    public ViburObjectWorker(CountDownLatch countDownLatch, long loop, PoolService<StringBuilder> viburPool) {
        super(countDownLatch, loop);
        this.viburPool = viburPool;
    }

    @Override
    public void doWork() {
        try {
            for (long i = 0; i < loop; i++) {
                StringBuilder stringBuilder = viburPool.take();

                if (Objects.nonNull(stringBuilder)) {
                    stringBuilder.append("*");
                    viburPool.restore(stringBuilder);
                } else {
                    errorCount++;
                }
            }
        } catch (Exception ex) {
            errorCount++;
        }
    }
}
