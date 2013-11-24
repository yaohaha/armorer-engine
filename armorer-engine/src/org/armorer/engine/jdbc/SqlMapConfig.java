package org.armorer.engine.jdbc;

import java.util.Map;

public class SqlMapConfig {

	private Map<String, Sql> sqlMap;
	private Map<String, Map<String, String>> resultMap;
	private Map<String, String> databaseMap;
	private Map<String, String> typeAliasMap;
	private Map<String, CacheModel> cacheModelMap;

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

	public Map<String, CacheModel> getCacheModelMap() {
		return cacheModelMap;
	}

	public void setCacheModelMap(Map<String, CacheModel> cacheModelMap) {
		this.cacheModelMap = cacheModelMap;
	}

}
