package tinypool;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public interface ObjectFactory<T> {

    T createObject();

    T resetObject(T oldObject);

    void destroyObject();


}
