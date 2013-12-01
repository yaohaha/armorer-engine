package org.armorer.engine.tmp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseTemplate extends BaseDao {

    protected String sql = null;
    
    protected String resultClassPath = null;
    
    protected Class<?> resultClass = null;
    
    // 存放sql，通过sqlID取得。
    protected HashMap<String, String> sqlMap;

    // 存放sql目标类的路径，通过sqlID取得。
    protected HashMap<String, String> resultClassPathMap;
    
    // 存放表的主键，通过sqlID取得。
    protected HashMap<String, String> primaryKeyMap;

    // 存放连接数据库相关的信息
    protected HashMap<String, String> databaseMap;
    
    // 存数据库表中的列的信息，以配置文件中字段名为键
    protected HashMap<String, String> columnMap;
    
    // 存数据库表中的列的信息，以配置文件中字段名为键
    protected HashMap<String, HashMap<String, String>> classAndColumnMap;
    
    protected void createClass(String id) {
//        SqlMapConfigParser parse = SqlMapConfigParser.getInstense();
//        sqlMap = parse.getSqlMap();
//        resultClassPathMap = parse.getResultClassPathMap();
//        databaseMap = parse.getDatabaseMap();
//        classAndColumnMap = parse.getClassAndColumnMap();
        sql = sqlMap.get(id);
        resultClassPath = resultClassPathMap.get(id);
        columnMap = classAndColumnMap.get(resultClassPath);
        try {
            resultClass = Class.forName(resultClassPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        initConnection(databaseMap);
    }

    protected void setValueToSql(Object obj, List<String> entityList) {
        List<String> valueList = new ArrayList<String>();
        for (int i = 0; i < entityList.size(); i++) {
            if (i % 2 != 0) {
                String item = entityList.get(i);
                try {
                    Field field = resultClass.getDeclaredField(item);
                    field.setAccessible(true);
                    if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                        valueList.add(field.get(obj).toString());
                    } else {
                        if (field.get(obj) == null) {
                            valueList.add("''");
                        } else {
                            valueList.add("'" + field.get(obj).toString() + "'");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                valueList.add(entityList.get(i));
            }
        }
        String valueStr = "";
        for (String item : valueList) {
            valueStr += item;
        }
        sql = valueStr;
    }

    protected List<String> parseSql() {
        List<String> entityList = new ArrayList<String>();
        String[] ss = sql.split("#");
        for (int i = 0; i < ss.length; i++) {
            entityList.add(ss[i]);
        }
        return entityList;
    }

}
