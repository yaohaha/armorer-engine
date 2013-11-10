package org.armorer.engine.jdbc;

import java.util.Map;

public class SqlMapConfig {

	  // 存放sql，通过sqlID取得。
	  private Map<String, Sql> sqlMap;
	  // 存放数据库和实体映射的信息
	  private Map<String, Map<String, String>> resultMap;
	  // 存放连接数据库相关的信息
	  private Map<String, String> databaseMap;
	  
	  private Map<String, String> typeAliasMap;
	    
	  public Map<String, Sql> getSqlMap() {
		    return sqlMap;
		}

		public void setSqlMap(Map<String, Sql> sqlMap) {
		    this.sqlMap = sqlMap;
		}

		public Map<String, Map<String, String>> getResultMap() {
		    return resultMap;
		}

		public void setResultMap(Map<String, Map<String, String>> resultMap) {
		    this.resultMap = resultMap;
		}

		public Map<String, String> getDatabaseMap() {
		    return databaseMap;
		}

		public void setDatabaseMap(Map<String, String> databaseMap) {
		    this.databaseMap = databaseMap;
		}

		public Map<String, String> getTypeAliasMap() {
			return typeAliasMap;
		}

		public void setTypeAliasMap(Map<String, String> typeAliasMap) {
			this.typeAliasMap = typeAliasMap;
		}
		
		
}
