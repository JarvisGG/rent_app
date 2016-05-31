package com.hc.core.update;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * Http通信工具类.
 * 
 * @author 李君波
 * @version 1.0 2014-11-11
 */
public class UtilHttp {
	/**
	 * 超时-单位[毫秒]
	 */
	private static final int CONN_TIMEOUT = 8000;

	/**
	 * 工具类禁止实例化.
	 */
	private UtilHttp() {
	}

	/**
	 * 异步发送请求-POST方法.
	 * 
	 * @param action
	 *            方法
	 * @param httpCallback
	 *            接口实现类的实例
	 */
	public static void sendPost(String action, HttpCallback httpCallback) {
		RequestParams params = httpCallback.onParams();
		HttpUtils httpUtils = new HttpUtils(CONN_TIMEOUT);
		httpUtils.send(HttpMethod.POST, action, params, new RequesterCallBack(httpCallback));
	}

	public static void sendPost(Context context, String action, HttpCallback httpCallback) {
		RequestParams params = httpCallback.onParams();
		params.addBodyParameter("version", UtilPhone.getAppVersionName(context));
		HttpUtils httpUtils = new HttpUtils(CONN_TIMEOUT);
		httpUtils.send(HttpMethod.POST, action, params, new RequesterCallBack(httpCallback));
	}

	/**
	 * @Title 异步发送请求-POST方法.
	 * @param action
	 *            方法
	 * @param flag
	 *            回调标志位
	 * @param httpCallback
	 *            接口实现类的实例
	 * @author 李君波
	 * @since 2014年11月20日 V 1.0
	 */
	public static void sendPost(String action, int flag, HttpCallback httpCallback) {
		RequestParams params = httpCallback.onParams();
		HttpUtils httpUtils = new HttpUtils(CONN_TIMEOUT);
		httpUtils.send(HttpMethod.POST, action, params, new RequesterCallBack(httpCallback, flag));
	}

	/**
	 * 多参数多回调
	 * 
	 * @Title: sendPost
	 * @param action
	 * @param flag
	 * @param httpCallback
	 *            void
	 * @author 景庆超
	 * @since 2015年1月8日 V 1.0
	 */
	public static void sendPost(String action, int flag, MulParamsHttpCallback httpCallback) {
		RequestParams params = httpCallback.onParams(flag);
		HttpUtils httpUtils = new HttpUtils(CONN_TIMEOUT);
		httpUtils.send(HttpMethod.POST, action, params, new MulParamsRequesterCallBack(httpCallback, flag));
	}
	/**
	 * TODO 可以根据业务需求动态的添加方法
	 */
}
