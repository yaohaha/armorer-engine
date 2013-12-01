package org.armorer.engine.tmp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.armorer.engine.util.CommonUtil;
import org.armorer.engine.util.PageUtil;

public class SelectTemplate extends BaseTemplate {

    private PageUtil pageUtil = null;

    
    public SelectTemplate() {
        pageUtil = new PageUtil();
    }
    
/**
 * 根据sqlID进行查询。
 * @param id sqlID
 * @param obj 实体类
 * @return 实体类的List
 */
    public List<?> queryForList(String id, Object obj) {
        return queryForList(id, obj,-1);
    }
    
    
    private static int LIST = 0;
    private static int SINGLE = 1;
    private static int COUNT = 2;
    
    private void query(String id, Object obj,int currentPage,int type ){
        //调用父类用来初始化执行sql所需要的变量
        createClass(id);
        List<Object> classList = new ArrayList<Object>();
        try {
            st = con.createStatement();
            if (obj != null) {
                this.parseEntity(obj);
                if(type == LIST){
                	sql = pageUtil.limitSql(sql , currentPage);
                }
                
                CommonUtil.showInfo(this.getClass().getName(), id +":" + sql);
                rs = st.executeQuery(sql);
                String entityStr = this.splitSql();
                if (entityStr.equals("*")) {
                    while (rs.next()) {
                        obj = getEntityFromFields();
                        classList.add(obj);
                    }
                } else {
                    List<List<String>> entityLists = parseSql(entityStr);
                    while (rs.next()) {
                        obj = getEntityFromList(entityLists);
                        classList.add(obj);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }
    
    /**
     * 根据sqlID进行查询。
     * @param id sqlID
     * @param obj 实体类
     * @return 实体类的List
     */
        public List<?> queryForList(String id, Object obj,int currentPage) {
            //调用父类用来初始化执行sql所需要的变量
            createClass(id);
            List<Object> classList = new ArrayList<Object>();
            try {
                st = con.createStatement();
                if (obj != null) {
                    this.parseEntity(obj);
                    sql = pageUtil.limitSql(sql , currentPage);
                    CommonUtil.showInfo(this.getClass().getName(), id +":" + sql);
                    rs = st.executeQuery(sql);
                    String entityStr = this.splitSql();
                    if (entityStr.equals("*")) {
                        while (rs.next()) {
                            obj = getEntityFromFields();
                            classList.add(obj);
                        }
                    } else {
                        List<List<String>> entityLists = parseSql(entityStr);
                        while (rs.next()) {
                            obj = getEntityFromList(entityLists);
                            classList.add(obj);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeAll();
            }
            return classList;
        }
        
    /**
     * 根据sqlID进行查询。
     * @param id sqlID
     * @param obj 实体类
     * @return 实体类
     */
        public Object queryForObject(String id, Object obj) {
            //调用父类用来初始化执行sql所需要的变量
            createClass(id);
            Object resultObj = null;
            try {
                st = con.createStatement();
                if (obj != null) {
                    this.parseEntity(obj);
                    System.out.println(sql);
                    rs = st.executeQuery(sql);
                    String entityStr = this.splitSql();
                    if (entityStr.equals("*")) {
                        if (rs.next()) {
                            resultObj = getEntityFromFields();
                        }
                    } else {
                        List<List<String>> entityLists = parseSql(entityStr);
                        if (rs.next()) {
                            resultObj = getEntityFromList(entityLists);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeAll();
            }
            return resultObj;
        }
        
        public int queryForCounts(String id, Object obj) {
            //调用父类用来初始化执行sql所需要的变量
            createClass(id);
            int count = 0;
            try {
                st = con.createStatement();
                if (obj != null) {
                    this.parseEntity(obj);
                }
                System.out.println(sql);
                rs = st.executeQuery(sql);
    			if(rs.next()){
    				count = rs.getInt(1);
    			}
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeAll();
            }
            return count;
        }
        
    private void parseEntity(Object obj) {
        String[] ss = sql.split("#");
        List<String> needFieldNameList = new ArrayList<String>();
        List<String> needFieldValueList = new ArrayList<String>();
        for (int i = 1; i < ss.length; i += 2) {
            needFieldNameList.add(ss[i].trim());
        }
        Field[] fields = null;
        fields = obj.getClass().getDeclaredFields();
        for (String item : needFieldNameList) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (type.equals(int.class) || type.equals(Integer.class)) {

                        if (field.getName().equals(item)) {
                            needFieldValueList.add(field.get(obj).toString());
                        }

                    } else if (type.equals(String.class)) {
                        if (field.getName().equals(item)) {
                            String value = field.get(obj).toString();
                            value = "'" + value + "'";
                            needFieldValueList.add(value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 1; i < ss.length; i += 2) {
            int j = (i - 1) / 2;
            String oldStr = "#" + ss[i] + "#";
            String newStr = needFieldValueList.get(j);
            sql = sql.replace(oldStr, newStr);
        }
    }

    protected String splitSql() {
        String entityStr = sql.split("SELECT")[1].split("FROM")[0].trim();
        return entityStr;
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

    protected Object getEntityFromFields() {
        Object obj = null;
        try {
            obj = resultClass.newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        Field[] fields = null;
        fields = resultClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.equals(int.class) || type.equals(Integer.class)) {
                    field.set(obj, rs.getInt(field.getName()));
                } else if (type.equals(String.class)) {
                    field.set(obj, rs.getString(field.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    protected Object getEntityFromList(List<List<String>> entityLists) {
        Object obj = null;
        try {
            obj = resultClass.newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        for (List<String> item : entityLists) {
            try {
                Field field = null;
                String columnField = null;
                if(columnMap!=null){
                    columnField = columnMap.get(item.get(1));
                }

                if(columnField != null){
                    field = resultClass.getDeclaredField(columnField);
                }else{
                    field = resultClass.getDeclaredField(item.get(1));
                }

                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.equals(int.class) || type.equals(Integer.class)) {
                    field.set(obj, rs.getInt(item.get(1)));
                } else if (type.equals(String.class)) {
                    field.set(obj, rs.getString(item.get(1)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

}
