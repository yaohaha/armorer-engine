package org.armorer.engine.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.armorer.engine.bean.Sql;
import org.armorer.engine.common.CommonUtil;

public class SqlMapExecutorDelegate {

    private SqlMapConfig sqlMapConfig;
    private boolean statementCacheEnabled;
    private SimpleDataSource simpleDataSource;
    CommonUtil commonUtil = new CommonUtil();

    // protected SqlExecutor sqlExecutor;

    /**
     * Default constructor
     */
    public SqlMapExecutorDelegate() {
        sqlMapConfig = new SqlMapConfig();
        // sqlExecutor = new SqlExecutor();
    }

    protected SessionScope beginSessionScope() {
        return new SessionScope();
    }

    protected void endSessionScope(SessionScope sessionScope) {
        sessionScope.cleanup();
    }

    /**
     * init the sessionScope after parse the sqlmapConfig.xml
     */
    public void init() {
        commonUtil.parseInlineSql(sqlMapConfig.getSqlMap(), sqlMapConfig.getTypeAliasMap());
        simpleDataSource = new SimpleDataSource(sqlMapConfig.getDatabaseMap());
    }

    // -- Basic Methods
    /**
     * Call an insert statement by ID
     * 
     * @param sessionScope - the session
     * @param id - the statement ID
     * @param param - the parameter object
     * @return - the generated key (or null)
     * @throws SQLException - if the insert fails
     * 
     */
    public Object insert(SessionScope sessionScope, String id, Object param) throws SQLException {
        int result = 0;
        startTransaction(sessionScope);
        Connection conn = sessionScope.getConnection();
        System.out.println(conn);
        Sql sql = this.sqlMapConfig.getSqlMap().get(id);
        List<Object> values = commonUtil.parseParameterValuesByObject(sql.getParameterList(), sql.getParameterClass(),
                param);
        try {
            PreparedStatement ps = prepareStatement(sessionScope, conn, sql.getTxt());
            for (int i = 0; i < values.size(); i++) {
                commonUtil.doPreparedStatement(ps, values.get(i), i + 1);
            }
            result = ps.executeUpdate();
            commit(sessionScope);

        } finally {
            end(sessionScope);
        }
        return result;
    }

    public int update(SessionScope sessionScope, String id, Object param) throws SQLException {
        int result = 0;
        startTransaction(sessionScope);
        Connection conn = sessionScope.getConnection();
        System.out.println(conn);
        Sql sql = this.sqlMapConfig.getSqlMap().get(id);
        List<Object> values = commonUtil.parseParameterValuesByObject(sql.getParameterList(), sql.getParameterClass(),
                param);
        try {
            PreparedStatement ps = prepareStatement(sessionScope, conn, sql.getTxt());
            for (int i = 0; i < values.size(); i++) {
                commonUtil.doPreparedStatement(ps, values.get(i), i + 1);
            }
            result = ps.executeUpdate();
            commit(sessionScope);

        } finally {
            end(sessionScope);
        }
        return result;
    }

    /**
     * Execute a delete statement
     * 
     * @param sessionScope - the session scope
     * @param id - the statement ID
     * @param param - the parameter object
     * @return - the number of rows deleted
     * @throws SQLException - if the delete fails
     */
    public int delete(SessionScope sessionScope, String id, Object param) throws SQLException {
        return update(sessionScope, id, param);
    }

    public Object queryForObject(SessionScope sessionScope, String id, Object paramObject) throws SQLException {
        return queryForObject(sessionScope, id, paramObject, null);
    }

    /**
     * Execute a select for a single object
     * 
     * @param sessionScope - the session scope
     * @param id - the statement ID
     * @param paramObject - the parameter object
     * @param resultObject - the result object (if not supplied or null, a new
     *        object will be created)
     * @return - the result of the query
     * @throws SQLException - if the query fails
     */
    public Object queryForObject(SessionScope sessionScope, String id, Object paramObject, Object resultObject)
            throws SQLException {
        Sql sql = this.sqlMapConfig.getSqlMap().get(id);
        startTransaction(sessionScope);
        Connection conn = sessionScope.getConnection();
        List<Object> values = commonUtil.parseParameterValuesByObject(sql.getParameterList(), sql.getParameterClass(),
                paramObject);
        try {
            PreparedStatement ps = prepareStatement(sessionScope, conn, sql.getTxt());
            for (int i = 0; i < values.size(); i++) {
                commonUtil.doPreparedStatement(ps, values.get(i), i + 1);
            }
            ResultSet rs = ps.executeQuery();
            
            commit(sessionScope);
            
            try {
                commonUtil.doReslutSet(rs,sql.getResultClass(),sql.getTxt(),sqlMapConfig.getResultMap().get("Account"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } finally {
            end(sessionScope);
        }
        Object object = null;
        // Sql sql = this.sqlMapConfig.getSqlMap().get(id);
        // if (sql.getCacheModelId() != null) {
        // CacheModel cacheModel =
        // this.sqlMapConfig.getCacheModelMap().get(sql.getCacheModelId());
        // object = cacheModel.getObject(id);
        // if (object == null) {
        // startTransaction(sessionScope);
        // Connection conn = sessionScope.getConnection();
        // List<Object> values =
        // commonUtil.parseParameterValuesByObject(sql.getParameterList(),
        // sql.getParameterClass(), paramObject);
        // try {
        // PreparedStatement ps = prepareStatement(sessionScope, conn,
        // sql.getTxt());
        // for (int i = 0; i < values.size(); i++) {
        // commonUtil.doPreparedStatement(ps, values.get(i), i + 1);
        // }
        // ResultSet result = ps.executeQuery();
        // commit(sessionScope);
        //
        // } finally {
        // end(sessionScope);
        // }
        //
        // cacheModel.putObject(id, object);
        // }
        // }
        return object;
    }

    private static void commit(SessionScope sessionScope) throws SQLException {
        Transaction trans = sessionScope.getTransaction();
        sessionScope.setCurrentState(Transaction.STATE_STARTED);
        trans.commit();
        sessionScope.setCurrentState(Transaction.STATE_COMMITTED);
    }

    private static void end(SessionScope sessionScope) throws SQLException {
        Transaction trans = sessionScope.getTransaction();
        try {
            if (trans != null) {
                if (sessionScope.getCurrentState() != Transaction.STATE_COMMITTED) {
                    try {
                        trans.rollback();
                    } finally {
                        sessionScope.closePreparedStatements();
                        trans.close();
                    }
                }
            }
        } finally {
            sessionScope.setTransaction(null);
            sessionScope.setCurrentState(Transaction.STATE_ENDED);
        }
    }

    private PreparedStatement prepareStatement(SessionScope sessionScope, Connection conn, String sql)
            throws SQLException {
        if (sessionScope.hasPreparedStatementFor(sql)) {
            return sessionScope.getPreparedStatement((sql));
        } else {
            PreparedStatement ps = conn.prepareStatement(sql);
            sessionScope.putPreparedStatement(this, sql, ps);
            return ps;
        }
    }

    public void startTransaction(SessionScope sessionScope) {
        Transaction transaction = sessionScope.getTransaction();
        if (transaction == null) {
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
