package com.hc.core.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class NewMessage {
	public final static int PUSH = 1;
	public final static int MAIL = 2;
	private AbHttpUtil http;
	private Sign sign;
	private SharedpfTools sharedpfTools;

	public NewMessage(Context context, final ImageView indicater, int type) {
		http = AbHttpUtil.getInstance(context);
		sign = new Sign(context);
		sharedpfTools = SharedpfTools.getInstance(context);
		sign.init();
		sign.addParam("news_status", type);
		AbRequestParams params = new AbRequestParams();
		params.put("news_status", type);
		// http.post(UrlConnector.MESSAGE_INDICATER +
		// sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign(),
		// params, new AbStringHttpResponseListener() {
		//
		// @Override
		// public void onStart() {
		// Log.v("unread", "start");
		// }
		//
		// @Override
		// public void onFinish() {
		// }
		//
		// @Override
		// public void onFailure(int statusCode, String content, Throwable
		// error) {
		// Log.v("unread", content + "");
		// }
		//
		// @Override
		// public void onSuccess(int statusCode, String content) {
		// Log.v("unread", content);
		// try {
		// switch (new JSONObject(content).getString("status")) {
		// case "1":
		// indicater.setVisibility(View.VISIBLE);
		// break;
		// case "2":
		// indicater.setVisibility(View.GONE);
		// break;
		// default:
		// break;
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		// });
	}

}
