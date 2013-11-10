package org.armorer.engine.jdbc;
//package org.armorer.engine.sqlmap.client;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
//import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
//import com.ibatis.sqlmap.engine.scope.SessionScope;
//
//public class SqlExecutor {
//
//	  /**
//	   * Execute an update
//	   *
//	   * @param statementScope    - the request scope
//	   * @param conn       - the database connection
//	   * @param sql        - the sql statement to execute
//	   * @param parameters - the parameters for the sql statement
//	   * @return - the number of records changed
//	   * @throws SQLException - if the update fails
//	   */
//	  public int executeUpdate(StatementScope statementScope, Connection conn, String sql, Object[] parameters) throws SQLException {
//	    ErrorContext errorContext = statementScope.getErrorContext();
//	    errorContext.setActivity("executing update");
//	    errorContext.setObjectId(sql);
//	    PreparedStatement ps = null;
//	    setupResultObjectFactory(statementScope);
//	    int rows = 0;
//	    try {
//	      errorContext.setMoreInfo("Check the SQL Statement (preparation failed).");
//	      ps = prepareStatement(statementScope.getSession(), conn, sql);
//	      setStatementTimeout(statementScope.getStatement(), ps);
//	      errorContext.setMoreInfo("Check the parameters (set parameters failed).");
//	      statementScope.getParameterMap().setParameters(statementScope, ps, parameters);
//	      errorContext.setMoreInfo("Check the statement (update failed).");
//	      ps.execute();
//	      rows = ps.getUpdateCount();
//	    } finally {
//	      closeStatement(statementScope.getSession(), ps);
//	    }
//	    return rows;
//	  }
//	  
//	  private static PreparedStatement prepareStatement(SessionScope sessionScope, Connection conn, String sql) throws SQLException {
//		    SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sessionScope.getSqlMapExecutor()).getDelegate();
//		    if (sessionScope.hasPreparedStatementFor(sql)) {
//		      return sessionScope.getPreparedStatement((sql));
//		    } else {
//		      PreparedStatement ps = conn.prepareStatement(sql);
//		      sessionScope.putPreparedStatement(delegate, sql, ps);
//		      return ps;
//		    }
//		  }
//}
