package org.armorer.engine.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * A Session based implementation of the Scope interface
 */
public class SessionScope {
  private static long nextId;
  private long id;
  // Used by Any
  private SqlMapClient sqlMapClient;
//  private SqlMapExecutor sqlMapExecutor;
//  private SqlMapTransactionManager sqlMapTxMgr;
//  private int requestStackDepth;
//  // Used by TransactionManager
//  private Transaction transaction;
//  private TransactionState transactionState;
//  // Used by SqlMapExecutorDelegate.setUserProvidedTransaction()
//  private TransactionState savedTransactionState;
  // Used by StandardSqlMapClient and GeneralStatement
//  private boolean inBatch;
  // Used by SqlExecutor
//  private Object batch;
  private boolean commitRequired;
  private Map preparedStatements;

  private int currentState;
   
    private Transaction transaction;
  /**
   * Default constructor
   */
  public SessionScope() {
    this.preparedStatements = new HashMap();

//    this.inBatch = false;
//    this.requestStackDepth = 0;
    this.id = getNextId();
  }

	public Connection getConnection() {
		return transaction.getConnection();
	}
  
  
  
  /**
   * Get the SqlMapClient for the session
   *
   * @return - the SqlMapClient
   */
  public SqlMapClient getSqlMapClient() {
    return sqlMapClient;
  }

  /**
   * Set the SqlMapClient for the session
   *
   * @param sqlMapClient - the SqlMapClient
   */
  public void setSqlMapClient(SqlMapClient sqlMapClient) {
    this.sqlMapClient = sqlMapClient;
  }


  /**
   * Getter to tell if a commit is required for the session
   *
   * @return - true if a commit is required
   */
  public boolean isCommitRequired() {
    return commitRequired;
  }

  /**
   * Setter to tell the session that a commit is required for the session
   *
   * @param commitRequired - the flag
   */
  public void setCommitRequired(boolean commitRequired) {
    this.commitRequired = commitRequired;
  }

  public boolean hasPreparedStatementFor(String sql) {
    return preparedStatements.containsKey(sql);
  }

  public boolean hasPreparedStatement(PreparedStatement ps) {
    return preparedStatements.containsValue(ps);
  }

  public PreparedStatement getPreparedStatement(String sql) throws SQLException {
    if (!hasPreparedStatementFor(sql))
      throw new SqlMapException("Could not get prepared statement.  This is likely a bug.");
    PreparedStatement ps = (PreparedStatement) preparedStatements.get(sql);
    return ps;
  }

  public void putPreparedStatement(SqlMapExecutorDelegate delegate, String sql, PreparedStatement ps) {
    if (delegate.isStatementCacheEnabled()) {
        if (hasPreparedStatementFor(sql))
          throw new SqlMapException("Duplicate prepared statement found.  This is likely a bug.");
        preparedStatements.put(sql, ps);
    }
  }

  public void closePreparedStatements() {
    Iterator keys = preparedStatements.keySet().iterator();
    while (keys.hasNext()) {
      PreparedStatement ps = (PreparedStatement) preparedStatements.get(keys.next());
      try {
        ps.close();
      } catch (Exception e) {
        // ignore -- we don't care if this fails at this point.
      }
    }
    preparedStatements.clear();
  }

  public void cleanup() {
    closePreparedStatements();
    preparedStatements.clear();
  }

  public boolean equals(Object parameterObject) {
    if (this == parameterObject) return true;
    if (!(parameterObject instanceof SessionScope)) return false;
    final SessionScope sessionScope = (SessionScope) parameterObject;
    if (id != sessionScope.id) return false;
    return true;
  }

  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  /**
   * Method to get a unique ID
   *
   * @return - the new ID
   */
  public synchronized static long getNextId() {
    return nextId++;
  }



public Transaction getTransaction() {
    return transaction;
}

public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
}

public int getCurrentState() {
	return currentState;
}

public void setCurrentState(int currentState) {
	this.currentState = currentState;
}

}

