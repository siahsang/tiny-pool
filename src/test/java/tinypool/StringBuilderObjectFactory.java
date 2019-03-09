package tinypool;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public class StringBuilderObjectFactory implements ObjectFactory<StringBuilder> {
    @Override
    public StringBuilder createObject() {
        return new StringBuilder();
    }

    @Override
    public StringBuilder resetObject(StringBuilder oldObject) {
        oldObject.setLength(0);
        return oldObject;
    }

    @Override
    public void destroyObject() {

    }
}
