package tinypool;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class ObjectPoolImplTest {

    private StringBuilderObjectFactory stringBuilderObjectFactory;

    @Before
    public void setUp() {
        stringBuilderObjectFactory = new StringBuilderObjectFactory();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTakeObjectFromPoolHappyPath() {
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(1, 10, stringBuilderObjectFactory);

        Optional<StringBuilder> stringBuilder = objectPool.tryTakeObject(5);

        Assert.assertTrue(stringBuilder.isPresent());
    }

    @Test
    public void testTakeObjectBlockForQueue() throws Exception {

    }

    @Test
    public void testTakeObjectFromPoolWhenTimeOutOccurred() {
        final long timeOut = TimeUnit.SECONDS.toMillis(4);
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(1, 2, stringBuilderObjectFactory);

        // empty pool
        objectPool.takeObject();
        objectPool.takeObject();

        Instant start = Instant.now();
        Optional<StringBuilder> stringValue = objectPool.tryTakeObject(timeOut);
        Instant finish = Instant.now();


        long timeElapsed = Duration.between(start, finish).toMillis();
        Assert.assertTrue("Time to wait is wrong", timeElapsed >= 4000 && timeElapsed <= 4005);
        Assert.assertFalse(stringValue.isPresent());

    }

    @Test
    public void testTakeFromPoolWhenAlreadyTerminated() throws Exception {
        final long timeOut = TimeUnit.SECONDS.toMillis(4);
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(1, 2, stringBuilderObjectFactory);

        // take item
        objectPool.takeObject();

        // terminate object
        objectPool.terminate();

        Exception exception = null;
        try {
            objectPool.takeObject();
        } catch (IllegalStateException e) {
            exception = e;
        }

        Assert.assertEquals(IllegalStateException.class, exception.getClass());

    }

    @Test
    public void testReturnObjectToPool() {
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(5, 10, stringBuilderObjectFactory);

        StringBuilder objectFromPool = objectPool.takeObject().get();

        objectPool.returnObject(objectFromPool);
        Assert.assertEquals(10, objectPool.remainingCapacity());
        Assert.assertEquals(5, objectPool.totalCreatedObject());

    }

    @Test
    public void testTerminatePool() {
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(1, 2, stringBuilderObjectFactory);
        objectPool.terminate();

        Assert.assertEquals(0, objectPool.remainingCapacity());
        Assert.assertEquals(0, objectPool.totalCreatedObject());
    }

    @Test
    public void testVariousMetrics() {
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(5, 10, stringBuilderObjectFactory);

        Assert.assertEquals(10, objectPool.remainingCapacity());
        Assert.assertEquals(5, objectPool.totalCreatedObject());

        // take one object & test
        Optional<StringBuilder> objectFromPool = objectPool.takeObject();
        Assert.assertTrue(objectFromPool.isPresent());

        Assert.assertEquals(9, objectPool.remainingCapacity());
        Assert.assertEquals(5, objectPool.totalCreatedObject());


        // take remaining object & test
        for (int i = 0; i < 9; i++) {
            objectPool.takeObject();
        }
        Assert.assertEquals(0, objectPool.remainingCapacity());
        Assert.assertEquals(10, objectPool.totalCreatedObject());

        // take from empty pool & test
        Optional<StringBuilder> emptyObject = objectPool.takeObject();
        Assert.assertFalse(emptyObject.isPresent());

        // return back one object & test
        objectPool.returnObject(objectFromPool.get());
        Assert.assertEquals(1, objectPool.remainingCapacity());
        Assert.assertEquals(10, objectPool.totalCreatedObject());


    }

}