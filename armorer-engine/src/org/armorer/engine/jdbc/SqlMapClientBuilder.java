package org.armorer.engine.jdbc;

import org.apache.log4j.PropertyConfigurator;
import org.armorer.engine.dom4j.parser.SqlMapConfigParser;

public class SqlMapClientBuilder {

    /**
     * Builds an SqlMapClient using.
     * 
     * @param reads an sql-map-config.xml file path.
     * @return An SqlMapClient instance.
     */
    public SqlMapClient buildSqlMapClient(String path) {
        PropertyConfigurator.configure (this.getClass().getResource("/conf/log4j.properties")) ;
        SqlMapClient sqlMapClient = new SqlMapConfigParser().parse(path);
        sqlMapClient.init();
        return sqlMapClient;
    }
}
