package tinypool;

import java.util.Optional;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public interface ObjectPool<T> {

    /**
     * Take an element from the pool and return to the caller.<br>
     * Wait for {@code timeOut} millisecond to object become available.
     *
     * @param timeOut Amount of time(millisecond) to wait for an object to become available in the pool
     * @return an element from the pool
     */
    Optional<T> takeObjectFromPool(long timeOut);

    /**
     * Take an element from the pool and return to the caller.<br>
     *
     * @return an element from the pool
     */
    Optional<T> takeObjectFromPool();

    /**
     * Return object to the pool
     *
     * @param element An element to return to the pool
     */
    void returnObjectToPool(T element);

    /**
     * Terminate pool
     */
    void terminatePool();

}
