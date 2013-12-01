package org.armorer.engine;

import java.sql.SQLException;

import org.armorer.engine.jdbc.SqlMapClient;
import org.armorer.engine.jdbc.SqlMapClientBuilder;

import com.mydomain.domain.Account;

public class SqlMapClientTest extends Thread {

    private SqlMapClient sqlMapClient;

    public SqlMapClientTest(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public void run() {
        Account account = new Account();
        account.setFirstName("aaa");
        account.setLastName("bbb");
        account.setEmailAddress("test@test.com");
        try {
            sqlMapClient.insert("insertAccount", account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        s1();
    }

    public static void s1(){
            SqlMapClientBuilder sqlMapClientBuilder = new SqlMapClientBuilder();
            SqlMapClient sqlMapClient = sqlMapClientBuilder.buildSqlMapClient(null);
            try {
                sqlMapClient.queryForObject("selectAccountById", 127
                        );
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    public static void i1() {
        SqlMapClientBuilder sqlMapClientBuilder = new SqlMapClientBuilder();
        SqlMapClient sqlMapClient = sqlMapClientBuilder.buildSqlMapClient(null);
        for (int i = 0; i < 100; i++) {
            SqlMapClientTest sqlMapClientTest = new SqlMapClientTest(
                    sqlMapClient);
            sqlMapClientTest.start();
        }
    }

    public static void i2() {
        SqlMapClientBuilder sqlMapClientBuilder = new SqlMapClientBuilder();
        SqlMapClient sqlMapClient = sqlMapClientBuilder.buildSqlMapClient(null);
        for (int i = 0; i < 100; i++) {
            try {
                Account account = new Account();
                account.setFirstName("aaa");
                account.setLastName("bbb");
                account.setEmailAddress("test@test.com");
                sqlMapClient.insert("insertAccount", account);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
