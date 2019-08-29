package tinypool.performance;

import tinypool.ObjectPool;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class TinyPoolWorker extends Worker {

    private ObjectPool<StringBuilder> tinyPool;

    public TinyPoolWorker(CountDownLatch countDownLatch, long loop, ObjectPool<StringBuilder> objectPool) {
        super(countDownLatch, loop);
        this.tinyPool = objectPool;
    }

    @Override
    public void doWork() {
        try {
            for (long i = 0; i < loop; i++) {
                Optional<StringBuilder> stringBuilder = tinyPool.takeObject();

                if (stringBuilder.isPresent()) {
                    stringBuilder.get().append("*");
                    tinyPool.returnObject(stringBuilder.get());
                } else {
                    errorCount++;
                }
            }
        } catch (Exception ex) {
            errorCount++;
        }
    }
}
