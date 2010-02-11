import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.pool.PoolableObjectFactory;

public class XmlParserPoolableObjectFactory implements PoolableObjectFactory {

    private SAXParserFactory parserFactory;

    public XmlParserPoolableObjectFactory() {
        parserFactory = SAXParserFactory.newInstance();
    }

    @Override
    public Object makeObject() throws Exception {
        return parserFactory.newSAXParser();
    }

    @Override
    public boolean validateObject(Object obj) {
        return true;
    }

    @Override
    public void activateObject(Object obj) throws Exception {
        // Do nothing
    }

    @Override
    public void destroyObject(Object obj) throws Exception {
        // Do nothing
    }

    @Override
    public void passivateObject(Object obj) throws Exception {
        // Do nothing
    }
}
