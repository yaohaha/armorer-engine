package org.armorer.engine;

import java.io.IOException;
import java.io.Reader;

import org.armorer.engine.jdbc.SqlMapClient;
import org.armorer.engine.jdbc.SqlMapClientBuilder;
import org.armorer.engine.jdbc.SqlMapConfigParser;

import com.mydomain.domain.Account;

import junit.framework.TestCase;

public class SqlMapClientTest  extends Thread{
   
	private SqlMapClient sqlMapClient;
	public SqlMapClientTest(SqlMapClient sqlMapClient){
		this.sqlMapClient = sqlMapClient;
	}
    
	public void run() {
		  Account account =new Account();
		  account.setFirstName("aaa");
		  account.setLastName("bbb");
		  account.setEmailAddress("test@test.com");
		sqlMapClient.insert("insertAccount", account);
    }
	

    public static void main(String[] args) {
    	m1();
    }
    
    public static void m1(){
    	SqlMapClientBuilder sqlMapClientBuilder= new SqlMapClientBuilder();
    	SqlMapClient sqlMapClient =  sqlMapClientBuilder.buildSqlMapClient(null);
    	for(int i = 0; i<1;i++){
        	SqlMapClientTest sqlMapClientTest=new SqlMapClientTest(sqlMapClient);
        	sqlMapClientTest.start();
    	}
    }
    
    public static void m2(){
    	SqlMapClientBuilder sqlMapClientBuilder= new SqlMapClientBuilder();
    	SqlMapClient sqlMapClient =  sqlMapClientBuilder.buildSqlMapClient(null);
    	for(int i = 0; i<100;i++){
        	sqlMapClient.insert(null, null);
    	}
    }
}
