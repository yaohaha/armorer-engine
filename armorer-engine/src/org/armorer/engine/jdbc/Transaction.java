package org.armorer.engine.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private SimpleDataSource simpleDataSource;
    
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
