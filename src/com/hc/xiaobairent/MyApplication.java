package com.hc.xiaobairent;

import com.umeng.socialize.PlatformConfig;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		// 微信 appid appsecret
		PlatformConfig.setWeixin("wx979529870885b03e", "e1d4fa9d9c4133d311d0c5d2ad67363d");
		// QQ appkey appsecret
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
	};
}
