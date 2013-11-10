package org.armorer.engine.jdbc;

import java.sql.SQLException;

public class MappedStatement {
	private Sql sql;

//	public int executeUpdate(StatementScope statementScope, Transaction trans,
//			Object parameterObject) throws SQLException {
//		Sql sql = getSql();
//		 rows = sqlExecuteUpdate(statementScope, trans.getConnection(), sqlString, parameters);
//	}

	public Sql getSql() {
		return sql;
	}

	public void setSql(Sql sql) {
		this.sql = sql;
	}
	
	

}
