package tinypool;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class ObjectPoolImpl<T> implements ObjectPool<T> {

    private final int minSize;

    private final int maxSize;

    private final AtomicInteger totalCreatedObjects = new AtomicInteger(0);

    private final AtomicInteger reamingCapacity = new AtomicInteger();

    private final ObjectFactory<T> objectFactory;

    private final ArrayBlockingQueue<T> objectPool;

    private final AtomicBoolean isTerminated = new AtomicBoolean(false);


    public ObjectPoolImpl(int minSize, int maxSize, ObjectFactory<T> objectFactory) {
        Objects.requireNonNull(objectFactory, "Object factory is mandatory");

        this.minSize = minSize;
        this.maxSize = maxSize;
        this.reamingCapacity.set(maxSize);
        this.objectFactory = objectFactory;

        this.objectPool = new ArrayBlockingQueue<>(maxSize);

        initPool();
    }


    private void initPool() {
        for (int i = 0; i < this.minSize; i++) {
            objectPool.add(objectFactory.createObject());
            totalCreatedObjects.incrementAndGet();

        }
    }

    @Override
    public Optional<T> takeObjectFromPool(final long timeOut) {
        try {
            T pooledObject = objectPool.poll(timeOut, TimeUnit.MILLISECONDS);
            if (!Objects.isNull(pooledObject)) {
                reamingCapacity.decrementAndGet();
                return Optional.of(pooledObject);
            }

        } catch (InterruptedException e) {
            // no-op
            Thread.currentThread().interrupt();
        }

        return Optional.empty();
    }

    @Override
    public Optional<T> takeObjectFromPool() {
        return takeObjectFromPool(-1);
    }

    @Override
    public void returnObjectToPool(T element) {
        if (objectPool.offer(element)) {
            reamingCapacity.incrementAndGet();
        }

    }

    @Override
    public int remainingCapacity() {
        return isTerminated.get() ? 0 : reamingCapacity.get();
    }

    @Override
    public int totalCreatedObject() {
        return isTerminated.get() ? 0 : totalCreatedObjects.get();
    }

    @Override
    public void terminatePool() {
        if (!isTerminated.getAndSet(true)) {
            while (Objects.nonNull(objectPool.peek())) {
                T polledObject = objectPool.poll();
                if (Objects.nonNull(polledObject)) {
                    objectFactory.destroyObject(polledObject);
                    totalCreatedObjects.decrementAndGet();
                    reamingCapacity.decrementAndGet();
                }
            }
        }


    }
}
