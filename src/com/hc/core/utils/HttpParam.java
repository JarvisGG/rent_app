package com.hc.core.utils;

import java.io.Serializable;

public class HttpParam implements Serializable, Comparable<HttpParam> {
	private static final long serialVersionUID = 1L;
	private String paramName;
	private Object paramValue;

	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Object getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

	public HttpParam() {
		super();
	}

	public HttpParam(String paramName, Object paramValue) {
		super();
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	@Override
	public int compareTo(HttpParam another) {
		return paramName.toLowerCase().compareTo(another.paramName.toLowerCase());
	}

}
