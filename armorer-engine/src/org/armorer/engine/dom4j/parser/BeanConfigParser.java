package org.armorer.engine.dom4j.parser;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.armorer.engine.bean.Sql;
import org.armorer.engine.jdbc.SqlMapExecutorDelegate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeanConfigParser {
    
    Logger logger = Logger.getLogger(this.getClass());
    private SqlMapExecutorDelegate delegate;
    private SAXReader reader = null;
    
    public BeanConfigParser(SqlMapExecutorDelegate delegate,SAXReader reader){
        this.delegate = delegate;
        this.reader = reader;
    }
    
    @SuppressWarnings("unchecked")
    public void parseSqlMap(String classPath) {
        Document document = null;
        Map<String, Sql> sqlMap = new HashMap<String, Sql>();
        Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
        Map<String, String> typeAliasMap = new HashMap<String, String>();
        URL url = this.getClass().getResource(classPath);
        if (url != null) {
            try {
                document = reader.read(url);
            } catch (DocumentException e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.error("*****" + classPath + ": is not find!*****");
            return;
        }

        Element sqlMapE = document.getRootElement();
        // String tableName = root.attribute("namespace").getText().trim();
        Iterator<Element> baseIt = sqlMapE.elementIterator();
        while (baseIt.hasNext()) {
            Element e = baseIt.next();
            if (e.getQName().getName().equals("select")) {
                doSelectSqlMap(e, sqlMap);
            } else if(e.getQName().getName().equals("insert")){
                doInsertSqlMap(e, sqlMap);
            } else if(e.getQName().getName().equals("update")){
                doUpdateSqlMap(e, sqlMap);
            } else if(e.getQName().getName().equals("delete")){
                doDeleteSqlMap(e, sqlMap);
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

    private void doSelectSqlMap(Element e, Map<String, Sql> sqlMap){
        doSqlMap(e,sqlMap,"select");
    }
    
    private void doInsertSqlMap(Element e, Map<String, Sql> sqlMap){
        doSqlMap(e,sqlMap,"insert");
    }
    
    private void doUpdateSqlMap(Element e, Map<String, Sql> sqlMap){
        doSqlMap(e,sqlMap,"update");
    }
    
    private void doDeleteSqlMap(Element e, Map<String, Sql> sqlMap){
        doSqlMap(e,sqlMap,"delete");
    }
    private void doSqlMap(Element e, Map<String, Sql> sqlMap,String type){
        String id = e.attributeValue("id");
        if (id != null) {
            id = id.trim();
        } else {
            logger.error("Sql bean id is null");
            return;
        }
        Sql sql = new Sql();
        sql.setId(id);
        sql.setType(type);
        sql.setStrParameterClass(e.attributeValue("parameterClass"));
        sql.setStrResultClass(e.attributeValue("resultClass"));
        sql.setTxt(e.getText());
        sqlMap.put(id, sql);
    }

    @SuppressWarnings("unchecked")
    private void doResultMap(Element e, Map<String, Map<String, String>> resultMap) {
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

    private void doTypeAliasMap(Element e, Map<String, String> typeAliasMap) {
        String alias = e.attributeValue("alias");
        String type = e.attributeValue("type");
        typeAliasMap.put(alias, type);
    }

}
