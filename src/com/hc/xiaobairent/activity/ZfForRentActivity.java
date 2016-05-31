package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;

public class ZfForRentActivity extends BaseActivity{

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.tv_housing_resources)
	private TextView tvHouseName;
	@BindView(id = R.id.tv_checkout_date)
	private TextView tvCheckoutDate;
	@BindView(id = R.id.next_btu, click = true)
	private Button sumbitBtn;
	
	private int id;
	private String house_name;
	
	private static final String EWNEW = 1+"";
	private static final String STOPRENT = 2+"";
	
	// 网络参数
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfForRentActivity.this);
	private AbHttpUtil http;
	private AbRequestParams params;
	private Sign sign;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_withdrawal_request);
		
	}
	
	@Override
	public void initData() {
		super.initData();
		initTab();
		Bundle data = getIntent().getExtras();
		id = data.getInt("id");
		house_name = data.getString("house");
		tvHouseName.setText(house_name);
		String date = data.getString("house_begin") + " 至 " + data.getString("house_end");
		tvCheckoutDate.setText(date);
	}
	
	private void initTab() {
		menuTitle.setText("退租申请");
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
			applyData();
			break;

		default:
			break;
		}
	}
	
	private void applyData() {
		sign = new Sign(ZfForRentActivity.this);
		sign.init();
		http = AbHttpUtil.getInstance(ZfForRentActivity.this);
		String url = UrlConnector.MYRENTS_OPERATION+id+"?access-token="
				+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
		
		params = new AbRequestParams();
		params.put("type", EWNEW);
		params.put("renew", "");
		params.put("_method", "put");
		
		http.post(url, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();				
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					int status = jsonObject.getInt("status");
					Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}

}
