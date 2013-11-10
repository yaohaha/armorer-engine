package org.armorer.engine.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class UpdateTemplate extends BaseTemplate {

	private static Logger logger = Logger.getLogger(UpdateTemplate.class
			.getName());

	public int insert(String sqlId, Object obj) {
		createClass(sqlId);
		List<String> entityList = parseSql();
		setValueToSql(obj, entityList);

		logger.info("sql : " + sql);
		try {
			st = con.createStatement();
			return st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return 0;
	}

	public int insertGetID(String sqlId, Object obj) {
		createClass(sqlId);
		List<String> entityList = parseSql();
		setValueToSql(obj, entityList);
		logger.info("sql : " + sql);
		int autoIncKeyFromApi = -1;
		try {
			st = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_UPDATABLE);
			st.executeUpdate(sql);

			rs = st.getGeneratedKeys();
			if (rs.next()) {
				autoIncKeyFromApi = rs.getInt(1);
				System.out.println("Key returned from getGeneratedKeys():"
						+ autoIncKeyFromApi);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return autoIncKeyFromApi;
	}

	public int update(String sqlId, Object obj) {
		createClass(sqlId);
		List<String> entityList = parseSql();
		setValueToSql(obj, entityList);
		logger.info("sql : " + sql);
		try {
			st = con.createStatement();
			return st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return 0;
	}

	public int delete(String sqlId, Object obj) {
		createClass(sqlId);
		List<String> entityList = parseSql();
		setValueToSql(obj, entityList);
		System.out.println(sql);
		logger.info("sql : " + sql);
		try {
			st = con.createStatement();
			return st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return 0;
	}

}
