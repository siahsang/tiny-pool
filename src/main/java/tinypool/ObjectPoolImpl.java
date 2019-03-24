package tinypool;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class ObjectPoolImpl<T> implements ObjectPool<T> {


    private final int minSize;

    private final int maxSize;

    private final ObjectFactory<T> objectFactory;

    private final BlockingQueue<T> objectPool;


    public ObjectPoolImpl(int minSize, int maxSize, ObjectFactory<T> objectFactory) {
        Objects.requireNonNull(objectFactory, "Object factory is mandatory");

        this.minSize = minSize;
        this.maxSize = maxSize;
        this.objectFactory = objectFactory;

        this.objectPool = new ArrayBlockingQueue<>(maxSize);

        initPool();
    }


    private void initPool() {
        for (int i = 0; i < this.minSize; i++) {
            objectPool.add(objectFactory.createObject());
        }
    }

    @Override
    public Optional<T> takeObjectFromPool(final long timeOut) {
        try {
            T pooledObject = objectPool.poll(timeOut, TimeUnit.MILLISECONDS);
            if (!Objects.isNull(pooledObject)) {
                return Optional.of(pooledObject);
            }

        } catch (InterruptedException e) {
            // no-op
            Thread.interrupted();
        }

        return Optional.empty();
    }

    @Override
    public Optional<T> takeObjectFromPool() {
        return takeObjectFromPool(-1);
    }

    @Override
    public void returnObjectToPool(T object) {

    }

    @Override
    public void terminatePool() {

    }
}
