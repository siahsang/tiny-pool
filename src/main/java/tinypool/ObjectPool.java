package tinypool;

import java.util.Optional;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public interface ObjectPool<T> {

    Optional<T> takeObjectFromPool(long waitMs);

    Optional<T> takeObjectFromPool();

    void returnObjectToPool(T object);

    void terminatePool();

}
