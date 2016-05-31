package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;

import android.os.Bundle;
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

public class ZfMyMessageDetailActivity extends BaseActivity{

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	// 内容
	@BindView(id = R.id.tv_message_title)
	private TextView tvMessageTitle;
	@BindView(id = R.id.tv_message_date)
	private TextView tvMessageDate;
	@BindView(id = R.id.tv_message_state)
	private TextView tvMessageState;
	@BindView(id = R.id.tv_message_content)
	private TextView tvMessageContent;
	
	// 网络参数
	private SharedpfTools sharedpfTools;
	private Sign sign;
	private KJHttp kjHttp;
	
	
	private int pId;
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_mymessage_detail);	
	}
	
	@Override
	public void initData() {
		sharedpfTools = SharedpfTools.getInstance(getApplicationContext());
		sign = new Sign(getApplicationContext());
		sign.init();
		kjHttp = new KJHttp();
		Bundle bundle = getIntent().getExtras();
		pId = bundle.getInt("id");
		applyData();
		initTab();
	}
	
	private void initTab() {
		menuTitle.setText("消息详情");
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
		String url = UrlConnector.MYMESSAGE_DETAIL + pId + "?access-token="
				+ sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		kjHttp.get(url, null, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject jsonObject = new JSONObject(t);
					tvMessageTitle.setText(jsonObject.getString("title"));
					tvMessageDate.setText(jsonObject.getString("add_time"));
					tvMessageState.setText(jsonObject.getString("read"));
					tvMessageContent.setText(jsonObject.getString("content"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}

}
