import org.apache.commons.pool.impl.GenericObjectPool;

public class XmlParserPool {

    private final GenericObjectPool pool;

    public XmlParserPool(int maxActive) {
        pool = new GenericObjectPool(new XmlParserPoolableObjectFactory(), maxActive,
                GenericObjectPool.WHEN_EXHAUSTED_BLOCK, 0);
    }

    public Object borrowObject() throws Exception {
        return pool.borrowObject();
    }

    public void returnObject(Object obj) throws Exception {
        pool.returnObject(obj);
    }
}
