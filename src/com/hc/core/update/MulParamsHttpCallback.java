package com.hc.core.update;

import com.lidroid.xutils.http.RequestParams;

/**
 * Http通信接口.
 * 
 * @author William Lee 
 * @version 1.0 2014-11-4
 */
public interface MulParamsHttpCallback {
	
    // XXX 加上获取参数的标识符
	public RequestParams onParams(int flag);
	
	public void onSuccess(String result,int flag);
	
}
