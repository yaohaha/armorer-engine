package org.armorer.engine.jdbc;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SqlMapConfigParser {


    private SAXReader reader = null;

    public static Logger logger = Logger.getLogger (SqlMapConfigParser.class.getName());

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
        }
        try {
            Document document = reader.read(this.getClass().getResource(path));
            Element sqlMapConfig = document.getRootElement();
            Iterator<Element> baseIt = sqlMapConfig.elementIterator();
            while (baseIt.hasNext()) {
                Element e = baseIt.next();
                if (e.getQName().getName().equals("transactionManager")) {
                    parseTransactionManager(e);
                } else if (e.getQName().getName().equals("sqlMap")) {
                    String classPath = e.attribute("resource").getValue();
                    parseSqlMap(classPath);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
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
            databaseMap.put(property.attributeValue("name"),
                    property.attributeValue("value"));
        }
        this.delegate.getSqlMapConfig().setDatabaseMap(databaseMap);
    }

    @SuppressWarnings("unchecked")
    private void parseSqlMap(String classPath) throws Exception {
        Document document = null;
        Map<String, Sql> sqlMap = new HashMap<String, Sql>();
        Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
        Map<String, String> typeAliasMap = new HashMap<String, String>();
        try {
            URL url = this.getClass().getResource(classPath);
            if (url == null) {
                System.out.println("*****" + classPath + ": is not find!*****");
                return;
            } else {
                document = reader.read(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("*****" + classPath + ": is not find!*****");
        }
        Element sqlMapE = document.getRootElement();
        // String tableName = root.attribute("namespace").getText().trim();
        Iterator<Element> baseIt = sqlMapE.elementIterator();
        while (baseIt.hasNext()) {
            Element e = baseIt.next();
            if (e.getQName().getName().equals("select")
                    || e.getQName().getName().equals("insert")
                    || e.getQName().getName().equals("update")
                    || e.getQName().getName().equals("delete")) {
                doSqlMap(e, sqlMap);
            } else if (e.getQName().getName().equals("resultMap")) {
                doResultMap(e, resultMap);
            } else if (e.getQName().getName().equals("typeAlias")) {
            	doTypeAliasMap(e, typeAliasMap);
            }
        }
        
        this.delegate.getSqlMapConfig().setSqlMap(sqlMap);
        this.delegate.getSqlMapConfig().setResultMap(resultMap);
        this.delegate.getSqlMapConfig().setTypeAliasMap(typeAliasMap);
    }

    private void doSqlMap(Element e, Map<String, Sql> sqlMap)
            throws Exception {
        String id = e.attributeValue("id");
        Sql sql = new Sql();
        sql.setId(id);
        sql.setStrParameterClass(e.attributeValue("parameterClass"));
        sql.setStrResultClass(e.attributeValue("resultClass"));
        sql.setTxt(e.getText());
        sqlMap.put(id, sql);
    }

    @SuppressWarnings("unchecked")
    private void doResultMap(Element e,Map<String, Map<String, String>> resultMap) {
        // String mapId = e.attributeValue("id");
        String mapClass = e.attributeValue("class");
        Iterator<Element> baseIt = e.elementIterator();
        Map<String, String> result = new HashMap<String, String>();
        while (baseIt.hasNext()) {
            Element e2 = baseIt.next();
            if (e2.getQName().getName().equals("result")) {
                String property = e2.attributeValue("property");
                String column = e2.attributeValue("column");
                result.put(column, property);
            }
        }
        resultMap.put(mapClass, result);
    }

    
    private void doTypeAliasMap(Element e, Map<String, String> typeAliasMap){
    	String alias = e.attributeValue("alias");
    	String type = e.attributeValue("type");
    	typeAliasMap.put(alias, type);
    }

}


