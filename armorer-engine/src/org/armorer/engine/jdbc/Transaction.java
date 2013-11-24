package org.armorer.engine.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private SimpleDataSource simpleDataSource;
    
    public static final int STATE_STARTED = 1;
    public static final int STATE_COMMITTED = 2;
    public static final int STATE_ENDED = 3;
    public static final int STATE_USER_PROVIDED = 4;

    
    public Transaction(SimpleDataSource simpleDataSource){
    	this.simpleDataSource = simpleDataSource;
    }
    
    public void initConnection() {
    	try {
			connection = simpleDataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public Connection getConnection() {
    	if(connection == null){
    		initConnection();
    	}
        return connection;
    }
    
	public void commit() throws SQLException {
		if (connection != null) {
			connection.commit();
		}
	}
	
	public void rollback() throws SQLException {
		if (connection != null) {
			connection.rollback();
		}
	}  
    
	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}
	
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }


    
    
}
