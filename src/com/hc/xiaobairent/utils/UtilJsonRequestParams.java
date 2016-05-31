package com.hc.xiaobairent.utils;

import java.util.Map;

import org.json.JSONObject;

import com.lidroid.xutils.http.RequestParams;

public class UtilJsonRequestParams {
	public static RequestParams getRequestParams(Map<String, Object> mapParams) {
		JSONObject jsonObject = new JSONObject(mapParams);
		RequestParams params = new RequestParams();
		params.addBodyParameter("jsonstr", jsonObject.toString());
		return params;
	}
}
