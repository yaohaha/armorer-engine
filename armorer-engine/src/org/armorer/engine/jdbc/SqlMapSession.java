package org.armorer.engine.jdbc;

import java.sql.SQLException;

public class SqlMapSession {
    protected SqlMapExecutorDelegate delegate;
    protected boolean closed;
    protected SessionScope sessionScope;

    /**
     * Constructor
     * 
     * @param client - the client that will use the session
     */
    public SqlMapSession(SqlMapClient client) {
        this.delegate = client.getDelegate();
        sessionScope = this.delegate.beginSessionScope();
        this.closed = false;
    }

    // public void init(){
    // this.delegate.init();
    // }

    public Object insert(String id, Object param) throws SQLException {
        return delegate.insert(sessionScope, id, param);
    }

    public int update(String id, Object param) throws SQLException {
        return delegate.update(sessionScope, id, param);
    }

    public int delete(String id, Object param) throws SQLException {
        return delegate.delete(sessionScope, id, param);
    }

    public Object queryForObject(String id, Object paramObject) throws SQLException {
        return delegate.queryForObject(sessionScope, id, paramObject);
    }

    /**
     * Start the session
     */
    public void open() {
        // sessionScope.setSqlMapTxMgr(this);
    }

    /**
     * Getter to tell if the session is still open
     * 
     * @return - the status of the session
     */
    public boolean isClosed() {
        return closed;
    }

    public void close() {
        // if (delegate != null && sessionScope != null)
        // delegate.endSessionScope(sessionScope);
        // if (sessionScope != null)
        // sessionScope = null;
        // if (delegate != null)
        // delegate = null;
        // if (!closed)
        // closed = true;
    }
}
