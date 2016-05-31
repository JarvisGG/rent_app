package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;

public class ZfMyAgentActivity extends BaseActivity{
	
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;	
	
	@BindView(id = R.id.tv_agent_name)
	private TextView tvAgentName;
	@BindView(id = R.id.tv_agent_num)
	private TextView tvAgentNum;
	@BindView(id = R.id.tv_agent_mobile)
	private TextView tvAgentMobile;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;

	private SharedpfTools sharedpfTools;
	private AbHttpUtil httpUtil;
	private Sign sign;
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_myagent);
	}
	
	@Override
	public void initData() {
		super.initData();
		sharedpfTools = SharedpfTools.getInstance(ZfMyAgentActivity.this);
		httpUtil = AbHttpUtil.getInstance(ZfMyAgentActivity.this);
		sign = new Sign(ZfMyAgentActivity.this);
		sign.init();
		initTab();
		applyData();
	}
	
	private void applyData() {
		String url = UrlConnector.MYAGENT + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		httpUtil.get(url, null, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					tvAgentName.setText(jsonObject.getString("name"));
					tvAgentNum.setText(jsonObject.getString("num"));
					tvAgentMobile.setText(jsonObject.getString("mobile"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void requestData() {
		 String url = UrlConnector.MYAGENT_CHANGE + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		 Log.e("changeagent", url);
		 httpUtil.get(url, null, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					Toast.makeText(ZfMyAgentActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		case R.id.next_btu:
			Intent intent = new Intent();
			requestData();
			
			break;
		default:
			break;
		}
	}
	
	private void initTab() {
		menuTitle.setText("我的经纪人");
	}

}
