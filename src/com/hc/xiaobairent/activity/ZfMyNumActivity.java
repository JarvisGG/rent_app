package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;

public class ZfMyNumActivity extends BaseActivity{
	
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.tv_mcenter_mynum)
	private TextView myNum;
	
	// 用户类型
	private SharedpfTools sharedpfTools;
	// 网络参数
	private KJHttp http;
	private Sign sign;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_mynum);		
	}
	
	@Override
	public void initData() {
		super.initData();
		http = new KJHttp();
		sharedpfTools = SharedpfTools.getInstance(getApplicationContext());
		sign = new Sign(getApplicationContext());
		sign.init();
		initTab();
		applyData();
	}
	
	private void initTab() {
		menuTitle.setText("我的编号");
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		default:
			break;
		}
	}
	
	private void applyData() {
		String url = UrlConnector.MYNUM + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		http.get(url, null, false, new HttpCallBack() {
			
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(ZfMyNumActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.e("dasdasdasdasdasdsad",t);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					myNum.setText(jsonObject.getString("num"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
}
