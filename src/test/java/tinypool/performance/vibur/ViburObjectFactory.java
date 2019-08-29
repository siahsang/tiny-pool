package tinypool.performance.vibur;

import org.vibur.objectpool.PoolObjectFactory;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class ViburObjectFactory implements PoolObjectFactory<StringBuilder> {
    @Override
    public StringBuilder create() {
        return new StringBuilder();
    }

    @Override
    public boolean readyToTake(StringBuilder obj) {
        return true;
    }

    @Override
    public boolean readyToRestore(StringBuilder obj) {
        return true;
    }

    @Override
    public void destroy(StringBuilder obj) {

    }
}
