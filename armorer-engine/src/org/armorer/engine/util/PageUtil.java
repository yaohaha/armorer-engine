package org.armorer.engine.util;

import java.util.ArrayList;
import java.util.List;

import org.armorer.engine.bean.Page;
import org.armorer.engine.common.Constant;

public class PageUtil {

    /**
     * 根据配置文件中的sql文来重新生成新的sql文，用于oracle分页
     * 
     * @param tableName 表名
     * @param sql 原sql文
     * @param key 主键
     * @param currentPage 当前页数
     * @return 新的sql文
     */
    public String limitSql(String sql, int currentPage) {
    	if(currentPage > 0){
            if (Constant.DATABASE_TYPE.equals(Constant.ORACLE)) {
//              int from = getFrom(currentPage);
//              int to = getTo(currentPage);
//              if(sql.contains(" ORDER BY ")){
//                  sql.split(" ORDER BY ");
//              }
//              StringBuffer sqlBuf=new StringBuffer(sql);
//              if(sql.contains(" WHERE ")){
//                  return sqlBuf.append(" AND ROWID IN (SELECT rid FROM (SELECT ROWNUM rn,rid FROM(SELECT ROWID rid FROM ")
//                  .append(tableName)
//                  .append(") WHERE ROWNUM <=")
//                  .append(to)
//                  .append(") WHERE rn >= ")
//                  .append(from)
//                  .append(")").toString();
//              }else{
//                  return sqlBuf.append(" WHERE ROWID IN (SELECT rid FROM (SELECT ROWNUM rn,rid FROM(SELECT ROWID rid," + key + " FROM " + tableName + " ORDER BY "
//                  + key + " DESC) WHERE ROWNUM <= " + to + ") WHERE rn >= " + from + ") ORDER BY " + key + " DESC").toString();
//              }
          	 return null;
          } else if (Constant.DATABASE_TYPE.equals(Constant.MYSQL)) {
              int from = getFrom(currentPage);
              return sql += " LIMIT " + from + " ," + Constant.ONE_PAGE_COUNT + "";
          } else {
              return null;
          }
    	}else{
    		return sql;
    	}

    }

    public int getFrom(int currentPage) {
        int from = (currentPage - 1) * Constant.ONE_PAGE_COUNT ;
        return from;
    }

    public int getTo(int currentPage) {
        int to = currentPage * Constant.ONE_PAGE_COUNT;
        return to;
    }

    public List<Page> getPageList(Page page, String tableName, String method, Object obj) {
        List<Page> pageList = new ArrayList<Page>();
//        SelectTemplate tem = new SelectTemplate();
//        int count = tem.doSelectCount(tableName, method, obj);
//        page.setCount(count);
//        pageList.add(page);
        return pageList;
    }
}
