package org.armorer.engine.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
//import com.baidu.bae.api.util.BaeEnv;
import java.net.URL;

public class BaseDao {

    protected Connection con = null;

    protected Statement st = null;

    protected ResultSet rs = null;

    public void initConnection(String driver, String url, String userName, String password) {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void initBaiduConnect(){
//        try {
//            /*****1. 譖ｿ謐｢荳ｺ菴��蟾ｱ逧�焚謐ｮ蠎灘錐�亥庄莉守ｮ｡逅�ｸｭ蠢�衍逵句芦��****/
//    		String databaseName = "zuvyvnmGOlxXVwBzqgre";
//			/******2. 莉守識蠅�序驥城㈹蜿門�謨ｰ謐ｮ蠎楢ｿ樊磁髴�ｦ∫噪蜿よ焚******/
//			String host = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_IP);
//			String port = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_SQL_PORT);
//			String username = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
//			String password = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
//			String driverName = "com.mysql.jdbc.Driver";
//			String dbUrl = "jdbc:mysql://";
//			String serverName = host + ":" + port + "/";
//          	String connName = dbUrl + serverName + databaseName;
//          
//			/******3. 謗･逹�ｿ樊磁蟷ｶ騾画叫謨ｰ謐ｮ蠎灘錐荳ｺdatabaseName逧�恪蜉｡蝎ｨ******/
//          	Class.forName(driverName);
//			con = DriverManager.getConnection(connName, username, password);
//          	st = con.createStatement();
//         } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
  
    public void initConnection(Map<String, String> databaseMap) {
        this.initConnection(databaseMap.get("driver"), databaseMap.get("url"), databaseMap.get("username"), databaseMap.get("password"));
//        this.initBaiduConnect();
    }

    public void closeAll() {
        try {
            // SQLSERVER AND ORACLE is not support rs and st close
            // if (rs != null && !rs.isClosed()) {
            // rs.close();
            // }
            rs = null;

            // if (st != null && !st.isClosed()) {
            // st.close();
            // }
            st = null;
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
