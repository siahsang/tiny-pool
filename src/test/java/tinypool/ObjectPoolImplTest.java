package tinypool;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

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
    public void takeObjectFromPool1() {
    }

    @Test
    public void returnObjectToPool() {
    }

    @Test
    public void terminatePool() {
    }
}