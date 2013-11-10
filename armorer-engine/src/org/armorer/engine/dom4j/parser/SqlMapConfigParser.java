package org.armorer.engine.dom4j.parser;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.armorer.engine.jdbc.SqlMapClient;
import org.armorer.engine.jdbc.SqlMapExecutorDelegate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SqlMapConfigParser {
    
    Logger logger = Logger.getLogger(this.getClass());
    private SAXReader reader = null;

    private SqlMapExecutorDelegate delegate;
    private SqlMapClient client;

    public SqlMapConfigParser() {
        delegate = new SqlMapExecutorDelegate();
        client = new SqlMapClient(delegate);
        reader = new SAXReader();
    }

    /**
     * read the sqlmapConfig file and parse the information
     */
    @SuppressWarnings("unchecked")
    public SqlMapClient parse(String path) {
        if (path == null) {
            path = "/conf/SqlMapConfig.xml";
            logger.debug("Use default SqlMapConfig file path : " + path);
        }
        try {
            Document document = reader.read(this.getClass().getResource(path));
            Element sqlMapConfig = document.getRootElement();
            Iterator<Element> baseIt = sqlMapConfig.elementIterator();
            BeanConfigParser beanConfigParser = new BeanConfigParser(delegate,reader);
            while (baseIt.hasNext()) {
                Element e = baseIt.next();
                if (e.getQName().getName().equals("transactionManager")) {
                    logger.debug("Start parse transactionManager");
                    parseTransactionManager(e);
                    logger.debug("End parse transactionManager");
                } else if (e.getQName().getName().equals("sqlMap")) {
                    String classPath = e.attribute("resource").getValue();
                    logger.debug("Start parse" + classPath);
                    beanConfigParser.parseSqlMap(classPath);
                    logger.debug("End parse" + classPath);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return client;
    }

    /**
     * parse the database information, init "databaseMap" "driver", "url",
     * "username", "password"
     * 
     * @param e
     */
    @SuppressWarnings("unchecked")
    private void parseTransactionManager(Element e) {
        Map<String, String> databaseMap = new HashMap<String, String>();
        // String transactionManagerType = e.attributeValue("type");
        Element dataSource = e.element("dataSource");
        // String dataSourceType = dataSource.attributeValue("type");
        Iterator<Element> properties = dataSource.elementIterator();
        while (properties.hasNext()) {
            Element property = properties.next();
            databaseMap.put(property.attributeValue("name"), property.attributeValue("value"));
        }
        this.delegate.getSqlMapConfig().setDatabaseMap(databaseMap);
    }

}
