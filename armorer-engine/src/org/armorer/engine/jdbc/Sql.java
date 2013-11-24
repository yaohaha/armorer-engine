package org.armorer.engine.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Sql {

	private String id;
	private List<String> parameterList;
	private List<String> resultList;
	
	private Class<?> parameterClass;
	private Class<?> resultClass;
	private String strParameterClass;
	private String strResultClass;
	private String cacheModelId;
	private String txt;

	public String getId() {
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public Class<?> getParameterClass() {
		return parameterClass;
	}

	public void setParameterClass(Class<?> parameterClass) {
		// if (parameterClass != null) {
		// parameterClass = parameterClass.trim();
		// }
		this.parameterClass = parameterClass;
	}

	public Class<?> getResultClass() {
		return resultClass;
	}

	public void setResultClass(Class<?> resultClass) {
		// if (resultClass != null) {
		// resultClass = resultClass.trim();
		// if(parameterClass == null){
		// parameterClass = resultClass;
		// }
		// }
		this.resultClass = resultClass;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public List<String> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<String> parameterList) {
		this.parameterList = parameterList;
	}
	
	public List<String> getResultList() {
		return resultList;
	}

	public void setResultList(List<String> resultList) {
		this.resultList = resultList;
	}

	public String getStrParameterClass() {
		return strParameterClass;
	}

	public void setStrParameterClass(String strParameterClass) {
		this.strParameterClass = strParameterClass;
	}

	public String getStrResultClass() {
		return strResultClass;
	}

	public void setStrResultClass(String strResultClass) {
		this.strResultClass = strResultClass;
	}

	public String getCacheModelId() {
		return cacheModelId;
	}

	public void setCacheModelId(String cacheModelId) {
		this.cacheModelId = cacheModelId;
	}

}
