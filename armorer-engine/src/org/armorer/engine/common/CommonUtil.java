package org.armorer.engine.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.armorer.engine.bean.Sql;

public class CommonUtil {

    private static final String PARAMETER_TOKEN = "#";
    private static final String PARAMETER_SIGN = "?";

    /**
     * 初始化SQL MAP 的入口函数，将从配置文件中的所有SQL及类都初始化好
     * 
     * @param sqlMap
     * @param typeAliasMap
     */
    public void parseInlineSql(Map<String, Sql> sqlMap, Map<String, String> typeAliasMap) {
        for (Sql sql : sqlMap.values()) {
            doParAndResClass(sql, typeAliasMap);
            doParlistAndSqltxt(sql);
            if (sql.getType().equals("select")) {
                // 处理select的sql,将需要查询的字段分割放入list中
                doSelectSql(sql);
            }
        }
    }

    /**
     * 将别名Map中的字符串转换成类，放入实体SQL中。
     * 
     * @param sql
     * @param typeAliasMap
     */
    private void doParAndResClass(Sql sql, Map<String, String> typeAliasMap) {
        String strParameterClass = sql.getStrParameterClass();
        String strResultClass = sql.getStrResultClass();
        Class<?> parameterClass = convertClassByStr(strParameterClass, typeAliasMap);
        Class<?> resultClass = convertClassByStr(strResultClass, typeAliasMap);
        sql.setParameterClass(parameterClass);
        sql.setResultClass(resultClass);
    }

    /**
     * 将别名的字符串转换成类
     * 
     * @param strClass
     * @param typeAliasMap
     * @return
     */
    private Class<?> convertClassByStr(String strClass, Map<String, String> typeAliasMap) {
        Class<?> resultClass = null;
        if (strClass != null) {
            strClass = strClass.trim();
            if (strClass.equals("int") || strClass.equals("Integer")) {
                strClass = "java.lang.Integer";
            } else if (strClass.equals("String")) {
                strClass = "java.lang.String";
            }
            // 判断别名map是否包含
            else if (typeAliasMap.containsKey(strClass)) {
                strClass = typeAliasMap.get(strClass);
            }
            try {
                resultClass = Class.forName(strClass);
            } catch (ClassNotFoundException exc) {
                exc.printStackTrace();
            }
        }
        return resultClass;
    }

    /**
     * 格式化sql，将sql中的#id#替换成？
     * 
     * @param sql
     */
    private void doParlistAndSqltxt(Sql sql) {
        String newStr = sql.getTxt();
        if (newStr != null && !newStr.isEmpty()) {
            newStr = newStr.trim().replace("\r", " ").replace("\n", " ");
        }
        Map<String, List<String>> map = StringTool.strTokenizerForMore(newStr, PARAMETER_TOKEN, PARAMETER_SIGN);
        for (String item : map.keySet()) {
            sql.setTxt(item);
            sql.setParameterList(map.get(item));
            break;
        }
    }

    private void doSelectSql(Sql sql) {
        List<String> resultList = new ArrayList<String>();
        String sqlStr = sql.getTxt();
        String entityStr = sqlStr.split("SELECT")[1].split("FROM")[0].trim();
        String[] ss = entityStr.split(",");
        for (String s : ss) {
            if (s.contains(" AS ")) {
                s = s.split(" AS ")[0].trim();
            }
            if (s.contains(".")) {
                s = s.split("\\.")[1].trim();
            }
            resultList.add(s);
        }
        sql.setResultList(resultList);
    }

    /**
     * 将Bean中的值以参数列表 中字段的顺序赋值给结果列表,如果是JDK类的则直接将结果赋值给结果列表。
     * 
     * @param parameterList 参数列表
     * @param parameterClass
     * @param obj 实体Bean
     * @return 结果参数列表
     */
    public List<Object> parseParameterValuesByObject(List<String> parameterList, Class<?> objClass, Object obj) {
        List<Object> values = new ArrayList<Object>();
        if (isJDKClass(obj)) {
            values.add(obj);
            return values;
        }
        for (int i = 0; i < parameterList.size(); i++) {
            String parameter = parameterList.get(i);
            String strMethod = "get" + parameter.substring(0, 1).toUpperCase() + parameter.substring(1);
            try {
                Method m = objClass.getMethod(strMethod);
                values.add(m.invoke(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    /**
     * 判断所传入的obj是否是jdk中的基础类
     * 
     * @param obj
     * @return
     */
    public boolean isJDKClass(Object obj) {
        boolean result = false;
        if (obj instanceof Integer || obj instanceof String || obj instanceof Double || obj instanceof Float
                || obj instanceof Long || obj instanceof Boolean || obj instanceof Date) {
            result = true;
        }
        return result;
    }

    /**
     * 将参数的值赋值给PreparedStatement用以执行SQL
     * 
     * @param prepStatement
     * @param param
     * @param index
     * @throws SQLException
     */
    public void doPreparedStatement(PreparedStatement prepStatement, Object param, int index) throws SQLException {
        if (param instanceof Integer) {
            int value = ((Integer) param).intValue();
            prepStatement.setInt(index, value);
        } else if (param instanceof String) {
            String s = (String) param;
            prepStatement.setString(index, s);
        } else if (param instanceof Double) {
            double d = ((Double) param).doubleValue();
            prepStatement.setDouble(index, d);
        } else if (param instanceof Float) {
            float f = ((Float) param).floatValue();
            prepStatement.setFloat(index, f);
        } else if (param instanceof Long) {
            long l = ((Long) param).longValue();
            prepStatement.setLong(index, l);
        } else if (param instanceof Boolean) {
            boolean b = ((Boolean) param).booleanValue();
            prepStatement.setBoolean(index, b);
        } else if (param instanceof Date) {
            Date d = (Date) param;
            prepStatement.setDate(index, d);
        }
    }

    public List<Object> doReslutSet(ResultSet rs, Class<?> resultClass, String sqlStr, Map<String, String> resultMap)
            throws Exception {
        List<Object> result = new ArrayList<Object>();
        Object resultObj = null;
        String entityStr = sqlStr.split("SELECT")[1].split("FROM")[0].trim();
        if (entityStr.equals("*")) {
            if (rs.next()) {
                resultObj = getEntityFromFields(rs, resultClass, resultMap);
            }
        } else {
            // List<List<String>> entityLists = parseSql(entityStr);
            // if (rs.next()) {
            // resultObj = getEntityFromList(rs,resultClass,entityLists);
            // }
        }
        result.add(resultObj);
        return result;
    }

    /**
     * 通过从数据库查找出的结果集,转变为java bean（SELECT *）
     * 
     * @param rs 结果集
     * @param resultClass 目标类
     * @param resultMap 配置文件中列名字段映射
     * @return
     */
    protected Object getEntityFromFields(ResultSet rs, Class<?> resultClass, Map<String, String> resultMap) {
        Object obj = null;
        try {
            obj = resultClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String item : resultMap.keySet()) {
            String strSetMethod = "set" + resultMap.get(item).substring(0, 1).toUpperCase()
                    + resultMap.get(item).substring(1);
            String strGetMethod = "get" + resultMap.get(item).substring(0, 1).toUpperCase()
                    + resultMap.get(item).substring(1);
            try {

                Method mGet = resultClass.getMethod(strGetMethod);
                Class<?> type = mGet.getReturnType();
                if (type.equals(int.class) || type.equals(Integer.class)) {
                    Method mSet = resultClass.getMethod(strSetMethod, Integer.class);
                    mSet.invoke(obj, rs.getInt(item));
                } else if (type.equals(String.class)) {
                    Method mSet = resultClass.getMethod(strSetMethod, String.class);
                    mSet.invoke(obj, rs.getString(item));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    private List<List<String>> parseSql(String entityStr) {
        List<List<String>> entityLists = new ArrayList<List<String>>();
        String[] ss = entityStr.split(",");
        for (String s : ss) {
            List<String> entityList = new ArrayList<String>();
            if (s.contains(" AS ")) {
                String[] strs = s.split(" AS ");
                entityList.add(strs[0].trim());
                entityList.add(strs[1].trim().replace("\"", ""));
            } else {
                String strField;
                if (s.contains(".")) {
                    // 这里不能直接对.进行分割，必须加上\\.才行
                    strField = s.split("\\.")[1].trim();
                } else {
                    strField = s.trim();
                }
                entityList.add(strField);
                entityList.add(strField);
            }
            entityLists.add(entityList);
        }
        return entityLists;
    }

    protected Object getEntityFromList(ResultSet rs, Class<?> resultClass, List<List<String>> entityLists) {
        Object obj = null;
        // try {
        // obj = resultClass.newInstance();
        // } catch (InstantiationException e1) {
        // e1.printStackTrace();
        // } catch (IllegalAccessException e1) {
        // e1.printStackTrace();
        // }
        // for (List<String> item : entityLists) {
        // try {
        // Field field = null;
        // String columnField = null;
        // if(columnMap!=null){
        // columnField = columnMap.get(item.get(1));
        // }
        //
        // if(columnField != null){
        // field = resultClass.getDeclaredField(columnField);
        // }else{
        // field = resultClass.getDeclaredField(item.get(1));
        // }
        //
        // field.setAccessible(true);
        // Class<?> type = field.getType();
        // if (type.equals(int.class) || type.equals(Integer.class)) {
        // field.set(obj, rs.getInt(item.get(1)));
        // } else if (type.equals(String.class)) {
        // field.set(obj, rs.getString(item.get(1)));
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        return obj;
        // }

    }

    public List<Object> parseResultSet(List<String> parameterList, Class<?> resultClass) {
        List<Object> values = new ArrayList<Object>();
        // for(int i =0;i<parameterList.size();i++){
        // String parameter = parameterList.get(i);
        // String strMethod ="get" + parameter.substring(0,1).toUpperCase() +
        // parameter.substring(1);
        //
        // try {
        // Method m = parameterClass.getMethod(strMethod);
        // values.add(m.invoke(obj));
        // } catch (IllegalAccessException e) {
        // e.printStackTrace();
        // } catch (IllegalArgumentException e) {
        // e.printStackTrace();
        // } catch (InvocationTargetException e) {
        // e.printStackTrace();
        // } catch (NoSuchMethodException e) {
        // e.printStackTrace();
        // } catch (SecurityException e) {
        // e.printStackTrace();
        // }
        // }
        return values;
    }
}
