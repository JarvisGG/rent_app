package com.hc.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import com.ab.http.AbRequestParams;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @ClassName: Sign
 * @Description: 生成sign签名
 * @author frank.fun@qq.com
 * @date 2016年3月1日 上午11:38:36
 */
public class Sign {
	private static final String ACCESS_TOKEN = "access-token";
	private static final String APPSERCERT = "appsercert";
	private static final String SIGN = "sign";
	private SharedpfTools sharedpfTools;
	private ArrayList<HttpParam> httpParams = new ArrayList<HttpParam>();
	private AbRequestParams params;

	/**
	 * @ClassName: Sign
	 * @Description: 实例化
	 * @deprecated 下一个项目将作为私有类隐藏
	 */
	public Sign(Context context) {
		sharedpfTools = SharedpfTools.getInstance(context);
	}

	/**
	 * 
	 * @Title: getInstance
	 * @Description: 实例化
	 * @param context
	 * @return Sign 返回类型
	 */
	public static Sign getInstance(Context context) {
		return new Sign(context);
	}

	/**
	 * @function 获取请求中的sign
	 * @return sign String
	 */
	public String getSign() {
		Collections.sort(httpParams);
		String sha1 = "";
		for (HttpParam httpParam : httpParams) {
			sha1 = sha1 + "&" + httpParam.getParamName() + "=" + httpParam.getParamValue();
		}
		Log.v("sign", sha1.substring(1));
		return new SHA1().getDigestOfString(sha1.substring(1).getBytes()).toLowerCase(Locale.ENGLISH);
	}

	public AbRequestParams getParams() {
		return params;
	}

	public String getAccessToken() {
		return sharedpfTools.getAccessToken();
	}

	/**
	 * 
	 * @Title: addParam
	 * @Description: 值为空时不参与签名
	 * @param key
	 * @param value
	 *            设定文件
	 */
	public void addParam(String key, String value) {
		params.put(key, value);
		if (value != null && !key.equals(ACCESS_TOKEN) && !key.equals(APPSERCERT) && !key.equals(SIGN)) {
			httpParams.add(new HttpParam(key, value));
		}
	}

	public void addParam(String key, int value) {
		httpParams.add(new HttpParam(key, value));
		params.put(key, value);
	}

	public void addParam(String key, float value) {
		httpParams.add(new HttpParam(key, value));
		params.put(key, value + "");
	}

	public void addParam(String key, double value) {
		httpParams.add(new HttpParam(key, value));
		params.put(key, value + "");
	}

	public void addParam(String key, long value) {
		httpParams.add(new HttpParam(key, value));
		params.put(key, value + "");
	}

	/**
	 * 
	 * @Title: addParam
	 * @Description: 文件不参与签名
	 * @param key
	 * @param value
	 */
	public void addParam(String key, File value) {
		params.put(key, value);
	}

	/**
	 * 
	 * @Title: init
	 * @Description: 初始化签名数据，清空原有内容并添加access_token,appsecret
	 */
	public void init() {
		clear();
		params = new AbRequestParams();
		httpParams.add(new HttpParam(ACCESS_TOKEN, sharedpfTools.getAccessToken()));
		httpParams.add(new HttpParam(APPSERCERT, sharedpfTools.getAppsercert()));
	}

	/**
	 * 
	 * @Title: clear
	 * @Description: 清空签名内所有数据
	 */
	public void clear() {
		httpParams.clear();
	}

}
