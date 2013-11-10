package org.armorer.engine.jdbc;


public class SqlMapClientBuilder {

	  /**
	   * Builds an SqlMapClient using.
	   *
	   * @param reads an sql-map-config.xml file path.
	   * @return An SqlMapClient instance.
	   */
	public SqlMapClient buildSqlMapClient(String path) {
		SqlMapClient sqlMapClient = new SqlMapConfigParser().parse(path);
		sqlMapClient.init();
		return sqlMapClient;
	}
}
