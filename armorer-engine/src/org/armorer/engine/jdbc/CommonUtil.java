package org.armorer.engine.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CommonUtil {

	private static final String PARAMETER_TOKEN = "#";
	private static final String PARAM_DELIM = ":";

	public void parseInlineSql(Map<String, Sql> sqlMap,
			Map<String, String> typeAliasMap) {
		for (Sql sql : sqlMap.values()) {
			doParAndResClass(sql, typeAliasMap);
			doParlistAndSqltxt(sql);
		}
	}

	private void doParAndResClass(Sql sql, Map<String, String> typeAliasMap) {
		String strParameterClass = sql.getStrParameterClass();
		String strResultClass = sql.getStrResultClass();
		Class<?> parameterClass = getClassByStr(strParameterClass, typeAliasMap);
		Class<?> resultClass = getClassByStr(strResultClass, typeAliasMap);
		sql.setParameterClass(parameterClass);
		sql.setResultClass(resultClass);
	}
	
	private Class<?> getClassByStr(String strClass,
			Map<String, String> typeAliasMap) {
		Class<?> resultClass = null;
		if (strClass != null) {
			strClass = strClass.trim();
			if (strClass.equals("int")) {
				strClass = "java.lang.Integer";
			} else if (strClass.equals("String")) {
				strClass = "java.lang.String";
			} else if (typeAliasMap.containsKey(strClass)) {
				strClass = typeAliasMap.get(strClass);
			}
			try {
				resultClass = Class.forName(strClass);
			} catch (ClassNotFoundException exc) {
				exc.printStackTrace();
			}
		}
		return resultClass;
	}

	private void doParlistAndSqltxt(Sql sql) {
		List<String> mappingList = new ArrayList<String>();
		String newSql = sql.getTxt();

		if (newSql != null && !newSql.isEmpty()) {
			newSql = newSql.trim();
			newSql = newSql.replace("\r", " ");
			newSql = newSql.replace("\n", " ");
		} else {
			// throw new Exception();
		}
		StringTokenizer parser = new StringTokenizer(newSql, PARAMETER_TOKEN,
				true);
		StringBuffer newSqlBuffer = new StringBuffer();

		String token = null;
		String lastToken = null;
		while (parser.hasMoreTokens()) {
			token = parser.nextToken();
			if (PARAMETER_TOKEN.equals(lastToken)) {
				if (PARAMETER_TOKEN.equals(token)) {
					newSqlBuffer.append(PARAMETER_TOKEN);
					token = null;
				} else {
					// ParameterMapping mapping = null;
					if (token.indexOf(PARAM_DELIM) > -1) {
						// mapping = oldParseMapping(token, parameterClass,
						// typeHandlerFactory);
					} else {
						// mapping = newParseMapping(token, parameterClass,
						// typeHandlerFactory);
					}
					mappingList.add(token);
					// mappingList.add(mapping);
					newSqlBuffer.append("?");
					token = parser.nextToken();
					if (!PARAMETER_TOKEN.equals(token)) {
						// throw new
						// SqlMapException("Unterminated inline parameter in mapped statement ("
						// + "statement.getId()" + ").");
					}
					token = null;
				}
			} else {
				if (!PARAMETER_TOKEN.equals(token)) {
					newSqlBuffer.append(token);
				}
			}

			lastToken = token;
		}
		newSql = newSqlBuffer.toString();
		sql.setTxt(newSql);
		sql.setParameterList(mappingList);
	}

	
	public List<Object> parseParameterValuesByObject(List<String> parameterList ,Class<?> parameterClass, Object obj){
		List<Object> values = new ArrayList<Object>();
		for(int i =0;i<parameterList.size();i++){
			String parameter = parameterList.get(i);
			String strMethod ="get" + parameter.substring(0,1).toUpperCase() + parameter.substring(1);

			try {
				Method m = parameterClass.getMethod(strMethod);
				values.add(m.invoke(obj));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return values;
	}
	
	public void doPreparedStatement(PreparedStatement prepStatement,
			Object param, int index) throws SQLException {
		if (param instanceof Integer) {
			int value = ((Integer) param).intValue();
			prepStatement.setInt(index, value);
		} else if (param instanceof String) {
			String s = (String) param;
			prepStatement.setString(index, s);
		} else if (param instanceof Double) {
			double d = ((Double) param).doubleValue();
			prepStatement.setDouble(index, d);
		} else if (param instanceof Float) {
			float f = ((Float) param).floatValue();
			prepStatement.setFloat(index, f);
		} else if (param instanceof Long) {
			long l = ((Long) param).longValue();
			prepStatement.setLong(index, l);
		} else if (param instanceof Boolean) {
			boolean b = ((Boolean) param).booleanValue();
			prepStatement.setBoolean(index, b);
		} else if (param instanceof Date) {
			Date d = (Date) param;
			prepStatement.setDate(index, d);
		}
	}
}
