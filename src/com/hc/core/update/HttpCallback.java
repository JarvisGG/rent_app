package com.hc.core.update;

import com.lidroid.xutils.http.RequestParams;

/**
 * Http通信接口.
 * 
 * @author William Lee 
 * @version 1.0 2014-11-4
 */
public interface HttpCallback {
	
    // XXX 加上获取参数的标识符
	public RequestParams onParams();
	
	public void onSuccess(String result,int flag);
	
}
