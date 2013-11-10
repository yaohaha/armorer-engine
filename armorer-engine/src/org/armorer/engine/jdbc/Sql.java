package org.armorer.engine.jdbc;

import java.util.List;

public class Sql {

    private String id;
    private List<String> parameterList;
    private Class<?> parameterClass;
    private Class<?> resultClass;
    private String strParameterClass;
    private String strResultClass;
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
//        if (parameterClass != null) {
//            parameterClass = parameterClass.trim();
//        }
        this.parameterClass = parameterClass;
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class<?> resultClass) {
//        if (resultClass != null) {
//            resultClass = resultClass.trim();
//            if(parameterClass == null){
//                parameterClass = resultClass;
//            }
//        }
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
    
    
    
}
