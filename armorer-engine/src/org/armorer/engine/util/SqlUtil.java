package org.armorer.engine.util;

public class SqlUtil {

	public static String sqlFormat(String sql) throws Exception{
        if (sql != null && !sql.isEmpty()) {
            sql = sql.trim();
            sql = sql.replace("\r", "");
            sql = sql.replace("\n", "");
        } else {
            throw new Exception();
        }
        return sql;
	}
}
