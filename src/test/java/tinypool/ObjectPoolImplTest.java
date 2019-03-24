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

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTakeObjectFromPoolHappyPath() {
        StringBuilderObjectFactory stringBuilderObjectFactory = new StringBuilderObjectFactory();
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(1, 10, stringBuilderObjectFactory);

        Optional<StringBuilder> stringBuilder = objectPool.takeObjectFromPool(5);

        Assert.assertTrue(stringBuilder.isPresent());
    }

    @Test
    public void testTakeObjectFromPoolWhenTimeOutOccurred() {
        final long timeOut = TimeUnit.SECONDS.toMillis(4);
        StringBuilderObjectFactory stringBuilderObjectFactory = new StringBuilderObjectFactory();
        ObjectPoolImpl<StringBuilder> objectPool = new ObjectPoolImpl<>(1, 2, stringBuilderObjectFactory);

        // empty pool
        objectPool.takeObjectFromPool();
        objectPool.takeObjectFromPool();

        Instant start = Instant.now();
        Optional<StringBuilder> stringBuilder = objectPool.takeObjectFromPool(timeOut);
        Instant finish = Instant.now();


        long timeElapsed = Duration.between(start, finish).toMillis();
        Assert.assertTrue("Time to wait is wrong", timeElapsed >= 4000 && timeElapsed <= 4005);
        Assert.assertTrue("Pool should be empty", !stringBuilder.isPresent());

    }

    @Test
    public void returnObjectToPool() {
    }

    @Test
    public void terminatePool() {
    }


}