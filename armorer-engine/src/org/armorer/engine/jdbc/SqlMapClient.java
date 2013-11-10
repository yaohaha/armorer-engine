package org.armorer.engine.jdbc;

import java.sql.SQLException;

public class SqlMapClient {

    /**
     * Delegate for SQL execution
     */
    public SqlMapExecutorDelegate delegate;

    protected ThreadLocal<SqlMapSession> localSqlMapSession = new ThreadLocal<SqlMapSession>();

    /**
     * Constructor to supply a delegate
     * 
     * @param delegate
     *            - the delegate
     */
    public SqlMapClient(SqlMapExecutorDelegate delegate) {
        this.delegate = delegate;
    }
    
	public void init(){
		delegate.init();
	}
	
    protected SqlMapSession getLocalSqlMapSession() {
        SqlMapSession sqlMapSession = (SqlMapSession) localSqlMapSession.get();
        if (sqlMapSession == null || sqlMapSession.isClosed()) {
            sqlMapSession = new SqlMapSession(this);
            localSqlMapSession.set(sqlMapSession);
        }
        return sqlMapSession;
    }


    public Object insert(String id, Object param) {
        return getLocalSqlMapSession().insert(id, param);
    }
    //
    // public Object insert(String id) throws SQLException {
    // return getLocalSqlMapSession().insert(id);
    // }
    //
    // public int update(String id, Object param) throws SQLException {
    // return getLocalSqlMapSession().update(id, param);
    // }
    //
    // public int update(String id) throws SQLException {
    // return getLocalSqlMapSession().update(id);
    // }
    //
    // public int delete(String id, Object param) throws SQLException {
    // return getLocalSqlMapSession().delete(id, param);
    // }
    //
    // public int delete(String id) throws SQLException {
    // return getLocalSqlMapSession().delete(id);
    // }
    //
    // public Object queryForObject(String id, Object paramObject) throws
    // SQLException {
    // return getLocalSqlMapSession().queryForObject(id, paramObject);
    // }
    //
    // public Object queryForObject(String id) throws SQLException {
    // return getLocalSqlMapSession().queryForObject(id);
    // }
    //
    // public Object queryForObject(String id, Object paramObject, Object
    // resultObject) throws SQLException {
    // return getLocalSqlMapSession().queryForObject(id, paramObject,
    // resultObject);
    // }
    //
    // public List queryForList(String id, Object paramObject) throws
    // SQLException {
    // return getLocalSqlMapSession().queryForList(id, paramObject);
    // }
    //
    // public List queryForList(String id) throws SQLException {
    // return getLocalSqlMapSession().queryForList(id);
    // }
    //
    // public List queryForList(String id, Object paramObject, int skip, int
    // max) throws SQLException {
    // return getLocalSqlMapSession().queryForList(id, paramObject, skip, max);
    // }
    //
    // public List queryForList(String id, int skip, int max) throws
    // SQLException {
    // return getLocalSqlMapSession().queryForList(id, skip, max);
    // }
    
    public SqlMapExecutorDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(SqlMapExecutorDelegate delegate) {
        this.delegate = delegate;
    }
}
