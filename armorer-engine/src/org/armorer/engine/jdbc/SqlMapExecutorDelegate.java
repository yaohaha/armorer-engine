package org.armorer.engine.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
public class SqlMapExecutorDelegate {

    private SqlMapConfig sqlMapConfig;
	private boolean statementCacheEnabled;
	private SimpleDataSource simpleDataSource;
	CommonUtil commonUtil = new CommonUtil();
//	protected SqlExecutor sqlExecutor;
	
	/**
	 * Default constructor
	 */
	public SqlMapExecutorDelegate() {
		sqlMapConfig = new SqlMapConfig();
//		 sqlExecutor = new SqlExecutor();
	}

	protected SessionScope beginSessionScope() {
		return new SessionScope();
	}

	protected void endSessionScope(SessionScope sessionScope) {
		sessionScope.cleanup();
	}
	
	/**
	 *  init the sessionScope after parse the sqlmapConfig.xml
	 */
	public void init(){
		commonUtil.parseInlineSql(sqlMapConfig.getSqlMap(),sqlMapConfig.getTypeAliasMap());
		simpleDataSource = new SimpleDataSource(sqlMapConfig.getDatabaseMap());
	}
	

	
	// -- Basic Methods
	/**
	 * Call an insert statement by ID
	 * 
	 * @param sessionScope
	 *            - the session
	 * @param id
	 *            - the statement ID
	 * @param param
	 *            - the parameter object
	 * @return - the generated key (or null)
	 * @throws SQLException
	 *             - if the insert fails
	 */
	public Object insert(SessionScope sessionScope ,String id, Object param){
		startTransaction(sessionScope);
		Connection conn = sessionScope.getConnection();
		Sql sql = this.sqlMapConfig.getSqlMap().get(id);
		List<Object> values = commonUtil.parseParameterValuesByObject(sql.getParameterList(),sql.getParameterClass(),param);
		try {
			PreparedStatement ps = prepareStatement(sessionScope,conn,sql.getTxt());
			for(int i = 0; i<values.size();i++){
				commonUtil.doPreparedStatement(ps,values.get(i),i+1);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	
	private PreparedStatement prepareStatement(SessionScope sessionScope,
			Connection conn, String sql) throws SQLException {
		if (sessionScope.hasPreparedStatementFor(sql)) {
			return sessionScope.getPreparedStatement((sql));
		} else {
			PreparedStatement ps = conn.prepareStatement(sql);
			sessionScope.putPreparedStatement(this, sql, ps);
			return ps;
		}
	}
	
	public void startTransaction(SessionScope sessionScope){
		Transaction transaction = sessionScope.getTransaction();
		if(transaction == null){
			transaction = new Transaction(simpleDataSource);
		}
		sessionScope.setTransaction(transaction);
	}

	
	public SqlMapConfig getSqlMapConfig() {
		return sqlMapConfig;
	}

	public void setSqlMapConfig(SqlMapConfig sqlMapConfig) {
		this.sqlMapConfig = sqlMapConfig;
	}

	public boolean isStatementCacheEnabled() {
		return statementCacheEnabled;
	}

	public void setStatementCacheEnabled(boolean statementCacheEnabled) {
		this.statementCacheEnabled = statementCacheEnabled;
	}

	public SimpleDataSource getSimpleDataSource() {
		return simpleDataSource;
	}

	public void setSimpleDataSource(SimpleDataSource simpleDataSource) {
		this.simpleDataSource = simpleDataSource;
	}

	
}
