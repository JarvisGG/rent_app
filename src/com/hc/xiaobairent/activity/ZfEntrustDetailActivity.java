package com.hc.xiaobairent.activity;

import org.kymjs.kjframe.ui.BindView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.MydeputeModel;

public class ZfEntrustDetailActivity extends BaseActivity{
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.username)
	private TextView username;
	@BindView(id = R.id.userphone)
	private TextView userphone;
	@BindView(id = R.id.tv_address)
	private TextView tvAddress;
	@BindView(id = R.id.tv_area)
	private TextView tvArea;
	@BindView(id = R.id.tv_rent)
	private TextView tvRent;
	@BindView(id = R.id.tv_remark)
	private TextView tvRemark;
	
	private SharedpfTools sharedpfTools;
	private AbHttpUtil httpUtil;
	private Gson gson;
	private Sign sign;
	private int deleteId;
	private MydeputeModel deputeModel;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_mydepute);
	}
	
	@Override
	public void initData() {
		super.initData();
		initTab();
		Bundle bundle = getIntent().getExtras();
		deleteId = bundle.getInt("deleteId");
		httpUtil = AbHttpUtil.getInstance(ZfEntrustDetailActivity.this);
		sign = new Sign(ZfEntrustDetailActivity.this);
		sign.init();
		sharedpfTools = SharedpfTools.getInstance(ZfEntrustDetailActivity.this);
		gson = new Gson();
		applyData();
	}
	
	private void applyData() {
		AbRequestParams params = new AbRequestParams();
		params.put("id", deleteId);
		sign.addParam("id", deleteId);
		Log.e("delete_id", deleteId+"");
		String url = UrlConnector.MYENTRUST_DETAIL + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		httpUtil.post(url, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfEntrustDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("detail", content);
				deputeModel = gson.fromJson(content, MydeputeModel.class);
				username.setText(deputeModel.getName());
				userphone.setText(deputeModel.getMobile());
				tvAddress.setText(deputeModel.getProvince_name()+"-"+deputeModel.getCity_name()+"-"+deputeModel.getDistrict_name());
				tvArea.setText(deputeModel.getArea_shuttle_name());
				tvRent.setText(deputeModel.getRental_shuttle_name());
				tvRemark.setText(deputeModel.getRemark());
			}
		});
	}
	
	private void initTab() {
		menuTitle.setText("委托详情");
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

}
